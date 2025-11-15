package com.liquotrack.stocksip.features.ordermanagement.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderRepository
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderResponse
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

    private val _salesOrders = MutableStateFlow<List<SalesOrderResponse>>(emptyList())
    val salesOrders: StateFlow<List<SalesOrderResponse>> = _salesOrders.asStateFlow()

    private val _salesOrder = MutableStateFlow<SalesOrderResponse?>(null)
    val salesOrder: StateFlow<SalesOrderResponse?> = _salesOrder.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun getSalesOrdersForAccount() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val accountId = tokenManager.getAccountId()
                val allOrders = repository.getAllOrders()

                val filtered = if (!accountId.isNullOrEmpty()) {
                    allOrders.filter { it.supplierId == accountId }
                } else emptyList()

                val toShow = if (filtered.isNotEmpty()) filtered else allOrders
                _salesOrders.value = toShow

                Log.d("SALES_VM", ">>> total=${allOrders.size} shown=${toShow.size} (filtered=${filtered.size})")

            } catch (e: Exception) {
                _error.value = e.message ?: "Error fetching sales orders"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createSalesOrderFromProcurement(purchaseOrderId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.createSalesOrderFromPurchaseOrder(purchaseOrderId)
                _salesOrder.value = response

                getSalesOrdersForAccount()

            } catch (e: Exception) {
                _error.value = e.message ?: "Error creating sales order"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
