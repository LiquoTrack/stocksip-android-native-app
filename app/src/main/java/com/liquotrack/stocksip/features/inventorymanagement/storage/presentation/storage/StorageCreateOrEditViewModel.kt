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
import kotlin.collections.map
import kotlin.collections.orEmpty
import kotlin.collections.plus
import kotlin.compareTo

@HiltViewModel
class StorageCreateOrEditViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    // StateFlow to hold the list of products
    private val _products = MutableStateFlow<ProductsWithCount?>(null)

    // Publicly exposed StateFlow for observing product data
    val products: StateFlow<ProductsWithCount?> = _products.asStateFlow()

    private val _productName = MutableStateFlow("")
    val productName: StateFlow<String> = _productName

    private val _productType = MutableStateFlow("")
    val productType: StateFlow<String> = _productType

    private val _brand = MutableStateFlow("")
    val brand: StateFlow<String> = _brand

    private val _unitPrice = MutableStateFlow(0.0)
    val unitPrice: StateFlow<Double> = _unitPrice

    private val _currencyCode = MutableStateFlow("")
    val currencyCode: StateFlow<String> = _currencyCode

    private val _minimumStock = MutableStateFlow(0)
    val minimumStock: StateFlow<Int> = _minimumStock

    private val _imageFile = MutableStateFlow<File?>(null)
    val imageFile: StateFlow<File?> = _imageFile

    private val _imageUrl = MutableStateFlow("")
    val imageUrl: StateFlow<String> = _imageUrl

    private val _selectedProduct = MutableStateFlow<ProductResponse?>(null)
    val selectedProduct: StateFlow<ProductResponse?> = _selectedProduct

    private val _editingProduct = MutableStateFlow<ProductResponse?>(null)
    val editingProduct: StateFlow<ProductResponse?> = _editingProduct

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _minimumStockError = MutableStateFlow<String?>(null)
    val minimumStockError: StateFlow<String?> = _minimumStockError.asStateFlow()

    fun updateProductName(value: String) { _productName.value = value }

    fun updateProductType(value: String) { _productType.value = value }

    fun updateBrand(value: String) { _brand.value = value }

    fun updateUnitPrice(value: Double) { _unitPrice.value = value }

    fun updateCurrencyCode(value: String) { _currencyCode.value = value }

    fun updateImageFile(value: File?) { _imageFile.value = value }

    fun updateMinimumStock(value: Int) {
        _minimumStock.value = value
        validateMinimumStock()
    }

    fun clearMinimumStockError() { _minimumStockError.value = null }

    /**
     * Loads the product data into the form for editing.
     * @param product The [ProductResponse] object containing the product data to be edited.
     */
    fun loadProductForEdit(product: ProductResponse) {
        _isLoading.value = true
        _editingProduct.value = product
        updateProductName(product.name)
        updateProductType(product.productType)
        updateBrand(product.brand)
        updateUnitPrice(product.unitPrice)
        updateCurrencyCode(product.currencyCode)
        updateImageFile(null)
        _isLoading.value = false
    }

    /**
     * Clears the product form fields.
     */
    fun clearForm() {
        _editingProduct.value = null
        _productName.value = ""
        _productType.value = ""
        _brand.value = ""
        _unitPrice.value = 0.0
        _currencyCode.value = ""
        _minimumStock.value = 0
        _imageFile.value = null
        _imageUrl.value = ""
    }

    /**
     * Saves the product data. If editing an existing product, it updates it; otherwise, it creates a new one.
     * @param onSuccess A lambda function to be called upon successful save operation.
     */
    fun saveProduct(
        isEditing: Boolean,
        productId: String? = null,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val accountId = tokenManager.getAccountId() ?: throw Exception("Account ID not found")

                val imageFile = _imageFile.value

                val productRequest = ProductRequest(
                    name = _productName.value,
                    productType = _productType.value,
                    brand = _brand.value,
                    unitPrice = _unitPrice.value,
                    currencyCode = _currencyCode.value,
                    minimumStock = _minimumStock.value,
                    supplierId = null
                )

                if (isEditing && productId != null) {
                    val updated = repository.updateProduct(
                        productRequest,
                        productId,
                        imageFile
                    )
                    _products.value = _products.value?.copy(
                        products = _products.value?.products.orEmpty().map {
                            if (it.id == updated.id) updated else it
                        }
                    )
                } else {
                    val created = repository.registerProduct(
                        productRequest,
                        accountId,
                        imageFile
                    )

                    _products.value = _products.value?.copy(
                        products = _products.value?.products.orEmpty() + created
                    )
                }

                clearForm()
                onSuccess()
            } catch (e: Exception) {
                Log.e("PRODUCT", "Error saving product", e)
            }
        }
    }

    /**
     * Validates the minimum stock inputs to ensure it is greater than zero.
     * @return True if the minimum stock inputs are valid, false otherwise.
     */
    private fun validateMinimumStock(): Boolean {
        return if (_minimumStock.value <= 0) {
            _minimumStockError.value = "Minimum stock must be greater than 0"
            false
        } else {
            _minimumStockError.value = null
            true
        }
    }
}