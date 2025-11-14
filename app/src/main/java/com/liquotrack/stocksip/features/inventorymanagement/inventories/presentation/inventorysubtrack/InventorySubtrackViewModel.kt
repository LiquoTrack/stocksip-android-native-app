package com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventorysubtrack

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryResponse
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventorySubtrackRequest
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.repositories.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel for managing the state and logic of the inventory subtraction feature.
 * It handles loading inventories, validating inputs, and saving inventory subtractions.
 *
 * @param repository The repository for inventory operations.
 */
@HiltViewModel
class InventorySubtrackViewModel @Inject constructor(
    private val repository: InventoryRepository
) : ViewModel() {

    private val _inventoryList = MutableStateFlow<List<InventoryResponse>>(emptyList())
    val inventoryList: StateFlow<List<InventoryResponse>> = _inventoryList.asStateFlow()

    private val _currentQuantity = MutableStateFlow(0)
    val currentQuantity: StateFlow<Int> = _currentQuantity.asStateFlow()

    private val _selectedProductId = MutableStateFlow<String?>(null)
    val selectedProductId: StateFlow<String?> = _selectedProductId.asStateFlow()

    private val _quantityToSubtrack = MutableStateFlow(0)
    val quantityToSubtrack: StateFlow<Int> = _quantityToSubtrack.asStateFlow()

    private val _expirationDate = MutableStateFlow<Date?>(null)
    val expirationDate: StateFlow<Date?> = _expirationDate.asStateFlow()

    private val _quantityError = MutableStateFlow<String?>(null)
    val quantityError: StateFlow<String?> = _quantityError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun clearQuantityError() { _quantityError.value = null }

    fun updateSelectedProductIdAndExpirationDate(productId: String?, date: Date?) {
        _selectedProductId.value = productId
        _expirationDate.value = date
    }

    // Updates the quantity to subtrack and validates it.
    fun validateAndUpdateQuantityToDecrease(quantityToSubtrack: Int) {
        _quantityToSubtrack.value = quantityToSubtrack
        validateQuantity()
    }

    /**
     * Loads the inventory list for a given warehouse ID.
     *
     * @param warehouseId The ID of the warehouse to load inventories from.
     */
    fun loadInventoryList(warehouseId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val inventories = repository.getAllInventoriesByWarehouseId(warehouseId)
            _inventoryList.value = inventories
            _isLoading.value = false
        }
    }

    /**
     * Saves the inventory subtraction for a given warehouse ID.
     *
     * @param warehouseId The ID of the warehouse to update inventory for.
     * @param onSuccess Callback function to be invoked upon successful inventory subtraction.
     */
    fun saveInventorySubtrack(
        warehouseId: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            if (validateQuantity()) {
                try {
                    val selectedProductId = _selectedProductId.value

                    val request = InventorySubtrackRequest(
                        quantityToSubtrack = _quantityToSubtrack.value,
                        expirationDate = _expirationDate.value
                    )

                    if (selectedProductId != null) {
                        repository.subtrackProductsFromWarehouseInventory(
                            warehouseId = warehouseId,
                            productId = selectedProductId,
                            inventory = request
                        )
                        onSuccess()
                    }
                } catch (e: Exception) {
                    Log.e("INVENTORY SUBTRACK FORM", "Error subtracting inventory: ${e.message}", e)
                }

            }
        }
    }

    /**
     * Validates the quantity inputs to ensure it is greater than zero and does not exceed the current quantity.
     *
     * @return True if the quantity inputs are valid, false otherwise.
     */
    private fun validateQuantity(): Boolean {
        return if (_quantityToSubtrack.value <= 0 && _currentQuantity.value - _quantityToSubtrack.value < 0) {
            _quantityError.value = "Quantity to subtrack must be greater than 0 and less than current quantity"
            false
        } else {
            _quantityError.value = null
            true
        }
    }
}
