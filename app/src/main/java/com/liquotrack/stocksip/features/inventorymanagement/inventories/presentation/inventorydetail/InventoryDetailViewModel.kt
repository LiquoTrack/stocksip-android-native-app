package com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventorydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryResponse
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.repositories.InventoryRepository
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryDetailViewModel @Inject constructor(
    private val repository: InventoryRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _selectedInventory = MutableStateFlow<InventoryResponse?>(null)
    val selectedInventory: StateFlow<InventoryResponse?> = _selectedInventory

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    /**
     * Fetches a specific inventory by its unique identifier
     *
     * @param inventoryId The identifier of the inventory to fetch
     */
    fun getInventoryById(inventoryId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val inventory = repository.getInventoryById(inventoryId)
            _selectedInventory.value = inventory
            _isLoading.value = false
        }
    }

    /**
     * Deletes the currently editing inventory by its ID and updates the inventory list.
     *
     * @param inventoryId The identifier of the inventory to delete
     * @param onSuccess Callback to be invoked upon successful deletion
     */
    fun deleteInventoryById(inventoryId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.deleteInventory(inventoryId)
                onSuccess()
            } finally {
                _isLoading.value = false
            }
        }
    }
}