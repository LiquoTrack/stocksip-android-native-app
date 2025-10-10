package com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.product

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.Product
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
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
class ProductViewModel @Inject constructor(private val repository: ProductRepository) : ViewModel() {

    // StateFlow to hold the list of products
    private val _products = MutableStateFlow<List<Product>>(emptyList())

    // Publicly exposed StateFlow for observing product data
    val products: MutableStateFlow<List<Product>> = _products

    /**
     * Fetches all products associated with a specific account ID.
     *
     * This function launches a coroutine in the ViewModel scope to perform the data fetching
     * operation asynchronously. The fetched products are then assigned to the _products StateFlow.
     * A log statement is included to help with debugging and verifying the fetched data.
     *
     * Note: The account ID is currently hardcoded and should be replaced with a dynamic value as needed.
     */
    fun getAllProductsByAccountId() {
        viewModelScope.launch {
            // TODO: Replace hardcoded accountId with actual value
            _products.value = repository.getAllProductsByAccountId("68e49ffad906e587b9a91e4b")
            Log.d("STORAGE", "Received: ${_products.value}" )
        }
    }

    // Initialize the ViewModel by fetching products
    init {
        getAllProductsByAccountId()
    }
}