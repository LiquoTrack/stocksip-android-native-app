package com.liquotrack.stocksip.features.ordermanagement.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalesOrdersViewModel @Inject constructor(
    private val repository: SalesOrderRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _supplierOrders = MutableStateFlow<List<SupplierOrderItemUi>>(emptyList())
    val supplierOrders: StateFlow<List<SupplierOrderItemUi>> = _supplierOrders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadSupplierOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val supplierId = tokenManager.getAccountId() ?: return@launch
                val response = repository.getOrdersBySupplierId(supplierId)
                _supplierOrders.value = response.orders.map { order ->
                    SupplierOrderItemUi(
                        id = order.id,
                        title = order.orderCode,
                        priceLabel = "-",
                        quantity = order.items.sumOf { it.quantityToSell },
                        status = order.status,
                        ownerEmail = order.buyer,
                        ownerPhone = "-",
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

    fun updateOrderStatus(orderId: String, selectedLabel: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val alias = when (selectedLabel.trim().uppercase()) {
                    "CONFIRMED", "CONFIRM" -> "CONFIRM"
                    "CANCELED", "CANCEL" -> "CANCEL"
                    else -> "PENDING"
                }
                repository.updateOrderStatus(orderId, alias, null)
                loadSupplierOrders()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
