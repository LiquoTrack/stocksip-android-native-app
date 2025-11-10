package com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.storage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductRequest
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductResponse
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductsWithCount
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories.ProductRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * ViewModel for managing product data in the inventory management system.
 *
 * This ViewModel interacts with the ProductRepository to fetch and manage product information.
 * It exposes a StateFlow of products that can be observed by the UI layer.
 *
 * @property repository The repository used to fetch product data.
 */
@HiltViewModel
class StorageViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    // StateFlow to hold the list of products
    private val _products = MutableStateFlow<ProductsWithCount?>(null)

    // Publicly exposed StateFlow for observing product data
    val products: StateFlow<ProductsWithCount?> = _products.asStateFlow()

    private val _isMaxReached = MutableStateFlow(false)
    val isMaxReached = _isMaxReached.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    /**
     * Fetches all products associated with a specific account ID.
     *
     * This function launches a coroutine in the ViewModel scope to perform the data fetching
     * operation asynchronously. The fetched products are then assigned to the _products StateFlow.
     * A log statement is included to help with debugging and verifying the fetched data.
     *
     * Note: The account ID is retrieved from the TokenManager.
     */
    fun getAllProductsByAccountId() {
        viewModelScope.launch {
            val accountId = tokenManager.getAccountId()
            accountId?.let { _products.value = repository.getAllProductsByAccountId(it) }
            Log.d("STORAGE", "Received: ${_products.value}" )
        }
    }

    /**
     * Validates if the maximum number of products has been reached.
     * @return True if the current number of products is less than the maximum allowed, false otherwise.
     */
    fun validateMaxProducts() {
        val data = _products.value
        if (data == null) {
            _isMaxReached.value = false
            return
        }

        val currentCount = data.total
        val maxAllowed = data.maxProductsAllowed

        _isMaxReached.value = currentCount >= maxAllowed
    }

    // Initialize the ViewModel by fetching products
    init {
        getAllProductsByAccountId()
    }
}