package com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryResponse
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.repositories.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing inventory data in the inventory management system.
 *
 * This ViewModel interacts with the InventoryRepository to fetch and manage inventory information.
 * It exposes a StateFlow of inventories that can be observed by the UI layer.
 *
 * @property repository The repository used to fetch inventory data.
 */
@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: InventoryRepository
) : ViewModel() {

    private val _inventories = MutableStateFlow<List<InventoryResponse>>(emptyList())
    val inventories: StateFlow<List<InventoryResponse>> = _inventories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    /**
     * Fetches all inventories associated with a specific warehouse ID.
     * This function launches a coroutine in the ViewModel scope to perform the data fetching
     * operation asynchronously. The fetched inventories are then assigned to the _inventories StateFlow.
     *
     * @param warehouseId The ID of the warehouse for which to fetch inventories.
     */
    fun getAllInventoriesByWarehouseId(warehouseId: String) {
        viewModelScope.launch {
            if (warehouseId.isEmpty()) return@launch
            _inventories.value = repository.getAllInventoriesByWarehouseId(warehouseId)
            Log.d("INVENTORIES", "Received: ${_inventories.value}" )
        }
    }
}