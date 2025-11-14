package com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventorytransfer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryResponse
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryTransferRequest
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.repositories.InventoryRepository
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseResponse
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehousesWithCount
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.repositories.WarehouseRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel for managing the state and logic of the inventory transfer feature.
 * It handles loading inventories and warehouses, validating inputs, and transferring products.
 *
 * @param repository The repository for inventory operations.
 * @param warehouseRepository The repository for warehouse operations.
 * @param tokenManager The token manager for retrieving account information.
 */
@HiltViewModel
class InventoryTransferViewModel @Inject constructor(
    private val repository: InventoryRepository,
    private val warehouseRepository: WarehouseRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _inventoryList = MutableStateFlow<List<InventoryResponse>>(emptyList())
    val inventoryList: StateFlow<List<InventoryResponse>> = _inventoryList.asStateFlow()

    private val _warehouseList = MutableStateFlow<List<WarehouseResponse>>(emptyList())
    val warehouseList: StateFlow<List<WarehouseResponse>> = _warehouseList.asStateFlow()

    private val _currentQuantity = MutableStateFlow(0)
    val currentQuantity: StateFlow<Int> = _currentQuantity.asStateFlow()

    private val _selectedProductId = MutableStateFlow<String?>(null)
    val selectedProductId: StateFlow<String?> = _selectedProductId.asStateFlow()

    private val _selectedWarehouseId = MutableStateFlow<String?>(null)
    val selectedWarehouseId: StateFlow<String?> = _selectedWarehouseId.asStateFlow()

    private val _quantityToTransfer = MutableStateFlow(0)
    val quantityToTransfer: StateFlow<Int> = _quantityToTransfer.asStateFlow()

    private val _expirationDate = MutableStateFlow<Date?>(null)
    val expirationDate: StateFlow<Date?> = _expirationDate.asStateFlow()

    private val _quantityError = MutableStateFlow<String?>(null)
    val quantityError: StateFlow<String?> = _quantityError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun clearQuantityError() {
        _quantityError.value = null
    }

    fun updateSelectedWarehouseId(warehouseId: String?) {
        _selectedWarehouseId.value = warehouseId
    }

    fun updateOnSelectedInventory(productId: String?, date: Date?, currentQty: Int) {
        _selectedProductId.value = productId
        _expirationDate.value = date
        _currentQuantity.value = currentQty
    }

    // Updates the quantity to transfer and validates it.
    fun validateAndUpdateQuantityToTransfer(quantityToTransfer: Int) {
        _quantityToTransfer.value = quantityToTransfer
        validateTransferQuantity()
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
            Log.e("INVENTORY TRANSFER FORM", "Received inventory list: $inventories")
            _isLoading.value = false
        }
    }

    /**
     * Loads the list of warehouses associated with the account.
     */
    fun loadWarehouseList() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val accountId = tokenManager.getAccountId()
                var warehouseWithCount = WarehousesWithCount(0, 0, emptyList())

                accountId?.let {
                    warehouseWithCount = warehouseRepository.getAllWarehousesByAccountId(it)
                }

                _warehouseList.value = warehouseWithCount.warehouses
                Log.e("INVENTORY TRANSFER FORM", "Received warehouses list: ${warehouseWithCount.warehouses}")
            } catch (e: Exception) {
                Log.e("INVENTORY TRANSFER FORM", "Error loading warehouse list: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Transfers products from one warehouse to another.
     *
     * @param originWarehouseId The ID of the warehouse from which products are being transferred.
     * @param onSuccess Callback function to be invoked upon successful transfer.
     */
    fun transferProduct(
        originWarehouseId: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            if (validateTransferQuantity()) {
                try {
                    val selectedProductId = _selectedProductId.value
                    val destinationWarehouseId = _selectedWarehouseId.value

                    if (selectedProductId != null && destinationWarehouseId != null) {
                        val request = InventoryTransferRequest(
                            destinationWarehouseId = destinationWarehouseId,
                            quantityToTransfer = _quantityToTransfer.value,
                            expirationDate = _expirationDate.value
                        )

                        repository.transferProductsToAnotherWarehouse(
                            originWarehouseId = originWarehouseId,
                            productToTransferId = selectedProductId,
                            inventoryRequest = request
                        )
                        onSuccess()
                    }
                } catch (e: Exception) {
                    Log.e("INVENTORY TRANSFER FORM", "Error transferring products: ${e.message}", e)
                }
            }
        }
    }

    /**
     * Validates the quantity inputs to ensure it is greater than zero and does not exceed the current quantity.
     *
     * @return True if the quantity inputs are valid, false otherwise.
     */
    private fun validateTransferQuantity(): Boolean {
        val qty = _quantityToTransfer.value
        val current = _currentQuantity.value

        return if (qty <= 0 || qty > current) {
            _quantityError.value =
                "Quantity must be greater than 0 and less than or equal to available quantity."
            false
        } else {
            _quantityError.value = null
            true
        }
    }
}
