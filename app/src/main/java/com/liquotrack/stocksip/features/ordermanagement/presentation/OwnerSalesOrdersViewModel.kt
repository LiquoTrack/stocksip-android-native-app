package com.liquotrack.stocksip.features.ordermanagement.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderRepository
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderResponse
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.presentation.OrderItemUi
import com.liquotrack.stocksip.shared.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerSalesOrdersViewModel @Inject constructor(
    private val repository: SalesOrderRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _ownerOrders = MutableStateFlow<List<SalesOrderResponse>>(emptyList())
    val ownerOrders: StateFlow<List<SalesOrderResponse>> = _ownerOrders.asStateFlow()

    private val _ownerOrdersUi = MutableStateFlow<List<OrderItemUi>>(emptyList())
    val ownerOrdersUi: StateFlow<List<OrderItemUi>> = _ownerOrdersUi.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadOwnerOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val ownerId = tokenManager.getUserIdFromToken() ?: tokenManager.getAccountId()
                if (ownerId.isNullOrBlank()) {
                    _error.value = "There is no active session (empty accountId). Please log in."
                    _isLoading.value = false
                    return@launch
                }
                val response = repository.getOrdersByLiquorStoreOwnerId(ownerId)
                _ownerOrders.value = response.orders
                _ownerOrdersUi.value = response.orders.map { order ->
                    val total = order.items.fold(0.0) { acc, it -> acc + (it.unitPrice * it.quantityToSell) }
                    val firstCurrency = order.items.firstOrNull()?.currency ?: "PEN"
                    val priceText = when (firstCurrency.uppercase()) {
                        "PEN", "S/.", "SOL", "SOLES" -> "S/. %.2f".format(total)
                        "USD", "$" -> "$. %.2f".format(total)
                        else -> "$firstCurrency %.2f".format(total)
                    }
                    OrderItemUi(
                        id = order.id,
                        code = order.orderCode,
                        title = order.orderCode,
                        priceLabel = priceText,
                        quantity = order.items.sumOf { it.quantityToSell },
                        status = order.status,
                        generatedAt = order.receiptDate
                    )
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun respondToDeliveryProposal(orderId: String, accept: Boolean, notes: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.respondToDeliveryProposal(orderId, accept, notes)
                loadOwnerOrders()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
