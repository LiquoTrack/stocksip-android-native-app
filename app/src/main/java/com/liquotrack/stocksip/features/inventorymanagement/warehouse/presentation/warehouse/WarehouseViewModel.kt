package com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.Warehouse
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.repositories.WarehouseRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing warehouse data and interactions.
 * It fetches and holds a list of warehouses associated with the user's account.
 * It uses Hilt for dependency injection to provide the necessary repository and token manager.
 */
@HiltViewModel
class WarehouseViewModel @Inject constructor(
    private val repository: WarehouseRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _warehouses = MutableStateFlow<List<Warehouse>>(emptyList())
    val warehouses: StateFlow<List<Warehouse>> = _warehouses

    /**
     * Fetches all warehouses associated with the current user's account ID.
     * The account ID is retrieved from the TokenManager.
     * The fetched warehouses are stored in a StateFlow to be observed by the UI.
     */
    fun getAlWarehousesByAccountId() {
        viewModelScope.launch {
            tokenManager.getAccountId()?.let { accountId ->
                _warehouses.value = repository.getAllByAccountIdWarehouses(accountId)
            } ?: Log.e("WarehouseViewModel", "Account ID is null")
        }
    }

    /**
     * Initializes the ViewModel by fetching the warehouses.
     */
    init {
        getAlWarehousesByAccountId()
    }
}