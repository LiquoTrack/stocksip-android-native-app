package com.liquotrack.stocksip.features.ordermanagement.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderRepository
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalesOrdersViewModel @Inject constructor(
    private val repository: SalesOrderRepository
) : ViewModel() {

    private val _salesOrder = MutableStateFlow<SalesOrderResponse?>(null)
    val salesOrder: StateFlow<SalesOrderResponse?> = _salesOrder.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * Crea una SalesOrder automáticamente desde una PurchaseOrder existente.
     */
    fun createSalesOrderFromProcurement(purchaseOrderId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repository.createSalesOrderFromPurchaseOrder(purchaseOrderId)
                _salesOrder.value = response
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al crear la Sales Order"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Obtiene una SalesOrder por su ID.
     */
    fun getSalesOrderById(orderId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repository.getOrderById(orderId)
                _salesOrder.value = response
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al obtener la Sales Order"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
