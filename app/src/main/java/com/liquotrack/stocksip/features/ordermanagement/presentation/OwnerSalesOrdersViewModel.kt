package com.liquotrack.stocksip.features.ordermanagement.presentation

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
class OwnerSalesOrdersViewModel @Inject constructor(
    private val repository: SalesOrderRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _ownerOrders = MutableStateFlow<List<SalesOrderResponse>>(emptyList())
    val ownerOrders: StateFlow<List<SalesOrderResponse>> = _ownerOrders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadOwnerOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Prefer userId from JWT (sid) to match backend liquorStoreOwnerId
                val ownerId = tokenManager.getUserIdFromToken() ?: tokenManager.getAccountId()
                if (ownerId.isNullOrBlank()) {
                    _error.value = "No hay sesión activa (accountId vacío). Inicia sesión."
                    _isLoading.value = false
                    return@launch
                }
                val response = repository.getOrdersByLiquorStoreOwnerId(ownerId)
                _ownerOrders.value = response.orders
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
