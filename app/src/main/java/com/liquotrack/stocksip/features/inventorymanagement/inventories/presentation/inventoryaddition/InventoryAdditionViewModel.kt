package com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventoryaddition

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryAdditionRequest
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryResponse
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.repositories.InventoryRepository
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductResponse
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductsWithCount
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories.ProductRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel for managing the state and logic of the inventory addition feature.
 * It handles loading products, validating inputs, and saving inventory additions.
 *
 * @param repository The repository for inventory operations.
 * @param productRepository The repository for product operations.
 * @param tokenManager The token manager for retrieving account information.
 */
@HiltViewModel
class InventoryAdditionViewModel @Inject constructor(
    private val repository: InventoryRepository,
    private val productRepository: ProductRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _productList = MutableStateFlow<List<ProductResponse>>(emptyList())
    val productList: StateFlow<List<ProductResponse>> = _productList.asStateFlow()

    private val _inventoryList = MutableStateFlow<List<InventoryResponse>>(emptyList())
    val inventoryList: StateFlow<List<InventoryResponse>> = _inventoryList.asStateFlow()

    private val _selectedProductId = MutableStateFlow<String?>(null)
    val selectedProductId: StateFlow<String?> = _selectedProductId.asStateFlow()

    private val _quantityToAdd = MutableStateFlow(0)
    val quantityToAdd: StateFlow<Int> = _quantityToAdd.asStateFlow()

    private val _expirationDate = MutableStateFlow<Date?>(null)
    val expirationDate: StateFlow<Date?> = _expirationDate.asStateFlow()

    private val _quantityError = MutableStateFlow<String?>(null)
    val quantityError: StateFlow<String?> = _quantityError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun updateQuantityToAdd(quantity: Int) {
        _quantityToAdd.value = quantity
        validateQuantity()
    }

    fun updateSelectedProductId(productId: String?) {
        _selectedProductId.value = productId
    }

    fun updateExpirationDate(date: Date?) { _expirationDate.value = date }

    fun clearQuantityError() { _quantityError.value = null }

    /**
     * Clears the form inputs to their default states.
     */
    fun clearForm() {
        _quantityToAdd.value = 0
        _expirationDate.value = null
        clearQuantityError()
    }

    /**
     * Loads the list of products associated with the current account.
     */
    fun loadProductList(warehouseId: String) {
        viewModelScope.launch {
            val accountId = tokenManager.getAccountId()
            var productsWithCount = ProductsWithCount(0,0, emptyList())
            val inventories = repository.getAllInventoriesByWarehouseId(warehouseId)

            accountId?.let {
                productsWithCount = productRepository.getAllProductsByAccountId(it)
            }

            _inventoryList.value = inventories
            _productList.value = productsWithCount.products
        }
    }

    /**
     * Saves the inventory addition to the specified warehouse.
     *
     * @param warehouseId The ID of the warehouse to which products are being added.
     * @param productId The ID of the product being added (optional).
     * @param onSuccess A callback function to be invoked upon successful addition.
     */
    fun saveInventoryAddition(
        warehouseId: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            if (validateQuantity()) {
                try {
                    val request = InventoryAdditionRequest(
                        quantityToAdd = _quantityToAdd.value,
                        expirationDate = _expirationDate.value
                    )

                    repository.addProductsToWarehouseInventory(
                        warehouseId = warehouseId,
                        productId = _selectedProductId.value.toString(),
                        inventory = request
                    )
                    onSuccess()
                } catch (e: Exception) {
                    Log.e("INVENTORY ADDITION FORM", "Error adding inventory: ${e.message}", e)
                }
            }
        }
    }

    /**
     * Validates the quantity inputs to ensure it is greater than zero.
     * @return True if the quantity inputs are valid, false otherwise.
     */
    private fun validateQuantity(): Boolean {
        return if (_quantityToAdd.value <= 0) {
            _quantityError.value = "Quantity to add must be greater than 0"
            false
        } else {
            _quantityError.value = null
            true
        }
    }
}