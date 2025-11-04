package com.liquotrack.stocksip.features.ordermanagement.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.domain.repositories.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalesOrdersViewModel @Inject constructor(
    private val repository: SalesOrderRepository,
    private val tokenManager: TokenManager,
    private val accountRepository: AccountRepository
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
                    val (email, phone) = try {
                        accountRepository.getAccountContacts(order.buyer)
                    } catch (_: Exception) {
                        Pair(null, null)
                    }
                    val total = order.items.fold(0.0) { acc, it -> acc + (it.unitPrice * it.quantityToSell) }
                    val firstCurrency = order.items.firstOrNull()?.currency ?: "PEN"
                    val priceText = when (firstCurrency.uppercase()) {
                        "PEN", "S/.", "SOL", "SOLES" -> "S/. %.2f".format(total)
                        "USD", "$" -> "$. %.2f".format(total)
                        else -> "$firstCurrency %.2f".format(total)
                    }
                    SupplierOrderItemUi(
                        id = order.id,
                        title = order.orderCode,
                        priceLabel = priceText,
                        quantity = order.items.sumOf { it.quantityToSell },
                        status = order.status,
                        ownerEmail = email ?: "-",
                        ownerPhone = phone ?: "-",
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
