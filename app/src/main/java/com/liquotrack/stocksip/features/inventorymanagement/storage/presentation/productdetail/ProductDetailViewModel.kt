package com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductResponse
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductsWithCount
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories.ProductRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.filter
import kotlin.collections.orEmpty

/**
 * ViewModel for managing product details, including fetching, editing and deleting products.
 *
 * @param repository The repository for product data operations
 */
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _selectedProduct = MutableStateFlow<ProductResponse?>(null)
    val selectedProduct: StateFlow<ProductResponse?> = _selectedProduct

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    fun showDeleteConfirmationDialog(show: Boolean) {
        _showDeleteDialog.value = show
    }

    /**
     * Fetches a specific product by its unique identifier
     * @param productId The identifier of the product to fetch
     */
    fun getProductById(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val product = repository.getProductById(productId)
            _selectedProduct.value = product
            _isLoading.value = false
        }
    }

    /**
     * Deletes the currently editing product by its ID and updates the product list.
     */
    fun deleteProductById(productId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.deleteProduct(productId)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}