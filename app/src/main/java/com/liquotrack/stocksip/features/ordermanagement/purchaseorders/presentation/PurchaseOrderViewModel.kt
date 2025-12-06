package com.liquotrack.stocksip.features.ordermanagement.purchaseorders.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderRepository
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

data class OrderProductUi(
    val name: String,
    val quantity: Int,
    val unitPrice: Double,
    val imageUrl: String?
)

data class OrderItemUi(
    val id: String,
    val code: String,
    val title: String,
    val priceLabel: String,
    val quantity: Int,
    val status: String,
    val generatedAt: String,
    val products: List<OrderProductUi> = emptyList()
)

@HiltViewModel
class PurchaseOrdersViewModel @Inject constructor(
    private val purchaseOrderRepository: PurchaseOrderRepository,
    private val salesOrderRepository: SalesOrderRepository,
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

    private val _createdPurchaseOrderId = MutableStateFlow<String?>(null)
    val createdPurchaseOrderId = _createdPurchaseOrderId.asStateFlow()

    fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val accountId = tokenManager.getAccountId()
                    ?: throw IllegalStateException("Account ID not found in token")

                val result = purchaseOrderRepository.getPurchaseOrders(accountId)

                result.onSuccess { orders ->
                    _ordersUi.value = orders.map { it.toUi() }
                }.onFailure { e ->
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
                val accountId = tokenManager.getAccountId()
                    ?: throw IllegalStateException("Account ID not found in token")

                val cartItems = cartRepository.getAllCartItems().first()
                if (cartItems.isEmpty()) throw IllegalStateException("Cart is empty.")

                if (addressIndex == null) throw IllegalStateException("Address index is null")

                val orderCode = generateOrderCode()

                val request = PurchaseOrderRequestDto(
                    orderCode = orderCode,
                    catalogIdBuyFrom = catalogIdBuyFrom,
                    addressIndex = addressIndex
                )

                val createdOrder = purchaseOrderRepository
                    .createPurchaseOrder(accountId, request)
                    .getOrElse { throw it }

                val orderId = createdOrder.id
                    ?: throw IllegalStateException("Order ID not returned")

                _createdPurchaseOrderId.value = orderId
                Log.d("PURCHASE_ORDER_VM", "Order created with ID: $orderId")

                cartItems.forEach { item ->
                    val itemRequest = PurchaseOrderItemRequestDto(
                        productId = item.productId,
                        quantity = item.quantity
                    )

                    purchaseOrderRepository.addItem(orderId, itemRequest)
                        .onFailure { e ->
                            Log.e("PURCHASE_ORDER_VM", "Failed adding item ${item.productId}", e)
                        }
                }

                purchaseOrderRepository.confirmOrder(orderId)
                    .onSuccess {
                        Log.d("PURCHASE_ORDER_VM", "Order $orderId confirmed")
                    }
                    .onFailure { e ->
                        Log.e("PURCHASE_ORDER_VM", "Order confirm failed", e)
                    }

                cartRepository.clearCart()
                loadOrders()

            } catch (e: Exception) {
                _error.value = e.message
                Log.e("PURCHASE_ORDER_VM", "Error creating full order", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun convertPurchaseToSales(purchaseOrderId: String) {
        viewModelScope.launch {
            try {
                Log.d("CONVERSION", ">>> Convirtiendo $purchaseOrderId a SalesOrder…")

                // Get the purchase order to get catalogIdBuyFrom
                val purchaseOrderResult = purchaseOrderRepository.getPurchaseOrderById(purchaseOrderId)

                val catalogIdBuyFrom = purchaseOrderResult
                    .getOrNull()
                    ?.catalogIdBuyFrom
                    ?: throw IllegalStateException("Catalog ID not found in purchase order")

                Log.d("CONVERSION", ">>> Catalog ID: $catalogIdBuyFrom")

                // Now create the sales order with the catalog ID
                val response = salesOrderRepository.createSalesOrderFromPurchaseOrder(
                    purchaseOrderId,
                    catalogIdBuyFrom
                )

                Log.d("CONVERSION", ">>> SalesOrder creada: ${response.id}")

            } catch (e: Exception) {
                Log.e("CONVERSION", "ERROR creando SalesOrder", e)
            }
        }
    }

    private fun generateOrderCode(): String {
        return "O" + (1000..9999).random()
    }

    private fun PurchaseOrderDto.toUi(): OrderItemUi = OrderItemUi(
        id = id ?: "",
        code = orderCode ?: "",
        title = "Catalog: ${catalogIdBuyFrom ?: "N/A"}",
        priceLabel = total?.let { "$" + "%.2f".format(it) } ?: "—",
        quantity = items?.sumOf { it.quantity ?: 0 } ?: 0,
        status = status ?: "PENDING",
        generatedAt = generationDate ?: "",
        products = items?.map {
            OrderProductUi(
                name = it.productName ?: "Unnamed",
                quantity = it.quantity ?: 0,
                unitPrice = it.unitPrice ?: 0.0,
                imageUrl = it.imageUrl
            )
        } ?: emptyList()
    )
}
