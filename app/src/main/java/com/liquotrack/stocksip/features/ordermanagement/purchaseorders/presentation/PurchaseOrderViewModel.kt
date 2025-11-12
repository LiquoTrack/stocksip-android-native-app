package com.liquotrack.stocksip.features.ordermanagement.purchaseorders.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.models.PurchaseOrderDto
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.models.PurchaseOrderItemRequestDto
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.models.PurchaseOrderRequestDto
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.domain.repositories.PurchaseOrderRepository
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.domain.repositories.AddressRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import com.liquotrack.stocksip.features.procurementordering.purchaseorders.domain.repositories.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrderItemUi(
    val id: String,
    val code: String,
    val title: String,
    val priceLabel: String,
    val quantity: Int,
    val status: String,
    val generatedAt: String
)

@HiltViewModel
class PurchaseOrdersViewModel @Inject constructor(
    private val purchaseOrderRepository: PurchaseOrderRepository,
    private val addressRepository: AddressRepository,
    private val tokenManager: TokenManager,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _ordersUi = MutableStateFlow<List<OrderItemUi>>(emptyList())
    val ordersUi = _ordersUi.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val accountId = tokenManager.getAccountId()
                    ?: throw IllegalStateException("Account ID not found in token")

                purchaseOrderRepository.getPurchaseOrders(accountId)
                    .onSuccess { orders ->
                        _ordersUi.value = orders.map { it.toUi() }
                    }
                    .onFailure { e ->
                        Log.e("PURCHASE_ORDER_VM", "Error fetching orders", e)
                        _error.value = e.message
                    }

            } catch (e: Exception) {
                Log.e("PURCHASE_ORDER_VM", "Unexpected error loading orders", e)
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createFullOrder(catalogIdBuyFrom: String, addressIndex: Int?) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("PURCHASE_ORDER_VM", "Starting order creation...")
                val accountId = tokenManager.getAccountId()
                    ?: throw IllegalStateException("Account ID not found in token")
                Log.d("PURCHASE_ORDER_VM", "Account ID: $accountId")

                val cartItems = cartRepository.getAllCartItems().first()
                Log.d("PURCHASE_ORDER_VM", "Cart items fetched: $cartItems")
                if (cartItems.isEmpty()) throw IllegalStateException("Cart is empty. Cannot create an order.")

                if (addressIndex == null) throw IllegalStateException("Address index is null")

                val orderCode = generateOrderCode()
                Log.d("PURCHASE_ORDER_VM", "Generated order code: $orderCode")

                val request = PurchaseOrderRequestDto(
                    orderCode = orderCode,
                    catalogIdBuyFrom = catalogIdBuyFrom,
                    addressIndex = addressIndex
                )
                Log.d("PURCHASE_ORDER_VM", "Order request: $request")

                val createdOrder = purchaseOrderRepository.createPurchaseOrder(accountId, request)
                    .getOrElse { throw it }
                val orderId = createdOrder.id ?: throw IllegalStateException("Order ID not returned")
                Log.d("PURCHASE_ORDER_VM", "Order created with ID: $orderId")

                // Añadir items al pedido
                cartItems.forEach { item ->
                    val itemRequest = PurchaseOrderItemRequestDto(productId = item.productId, quantity = item.quantity)
                    Log.d("PURCHASE_ORDER_VM", "Adding item to order: $itemRequest")
                    purchaseOrderRepository.addItem(orderId, itemRequest).onFailure { e ->
                        Log.e("PURCHASE_ORDER_VM", "Error adding item ${item.productId}", e)
                    }.onSuccess {
                        Log.d("PURCHASE_ORDER_VM", "Item ${item.productId} added successfully")
                    }
                }

                Log.d("PURCHASE_ORDER_VM", "All items added, confirming order...")
                purchaseOrderRepository.confirmOrder(orderId).onFailure { e ->
                    Log.e("PURCHASE_ORDER_VM", "Error confirming order", e)
                }.onSuccess {
                    Log.d("PURCHASE_ORDER_VM", "Order $orderId confirmed successfully")
                }

                cartRepository.clearCart()
                Log.d("PURCHASE_ORDER_VM", "Cart cleared after order creation")
                loadOrders()
                Log.d("PURCHASE_ORDER_VM", "Order list reloaded")

            } catch (e: Exception) {
                Log.e("PURCHASE_ORDER_VM", "Error creating full order", e)
                _error.value = e.message
            } finally {
                _isLoading.value = false
                Log.d("PURCHASE_ORDER_VM", "createFullOrder finished, isLoading=false")
            }
        }
    }



    private fun generateOrderCode(): String {
        val number = (1000..9999).random()
        return "O$number"
    }

    private fun PurchaseOrderDto.toUi(): OrderItemUi = OrderItemUi(
        id = id ?: "",
        code = orderCode ?: "",
        title = "Catalog: ${catalogIdBuyFrom ?: "N/A"}",
        priceLabel = total?.let { "$" + "%.2f".format(it) } ?: "—",
        quantity = items?.sumOf { it.quantity ?: 0 } ?: 0,
        status = status ?: "PENDING",
        generatedAt = generationDate ?: ""
    )
}
