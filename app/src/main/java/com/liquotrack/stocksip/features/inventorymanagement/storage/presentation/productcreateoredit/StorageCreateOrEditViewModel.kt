package com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.productcreateoredit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductRequest
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductResponse
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories.BrandRepository
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories.ProductRepository
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories.ProductTypeRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * ViewModel for creating or editing a product in storage.
 *
 * @param repository The [ProductRepository] for managing product data.
 * @param brandRepository The [BrandRepository] for fetching brand data.
 * @param tokenManager The [TokenManager] for managing authentication tokens.
 */
@HiltViewModel
class StorageCreateOrEditViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val brandRepository: BrandRepository,
    private val typeRepository: ProductTypeRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _brands = MutableStateFlow<List<String>>(emptyList())
    val brands: StateFlow<List<String>> = _brands.asStateFlow()

    private val _productTypes = MutableStateFlow<List<String>>(emptyList())
    val productTypes: StateFlow<List<String>> = _productTypes.asStateFlow()

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

    private val _content = MutableStateFlow(0.0)
    val content: StateFlow<Double> = _content

    private val _imageFile = MutableStateFlow<File?>(null)
    val imageFile: StateFlow<File?> = _imageFile

    private val _imageUrl = MutableStateFlow("")
    val imageUrl: StateFlow<String> = _imageUrl

    private val _selectedProduct = MutableStateFlow<ProductResponse?>(null)
    val selectedProduct: StateFlow<ProductResponse?> = _selectedProduct

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _minimumStockError = MutableStateFlow<String?>(null)
    val minimumStockError: StateFlow<String?> = _minimumStockError.asStateFlow()

    private val _contentError = MutableStateFlow<String?>(null)
    val contentError: StateFlow<String?> = _contentError.asStateFlow()

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

    fun updateProductContent(value: Double) {
        _content.value = value
        validateProductContent()
    }

    fun clearMinimumStockError() { _minimumStockError.value = null }

    fun clearContentError() { _contentError.value = null }

    /**
     * Fetches a product by its ID and updates the selected product state.
     *
     * @param productId The ID of the product to be fetched.
     */
    fun getProductById(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val product = repository.getProductById(productId)
                _selectedProduct.value = product
            } catch (e: Exception) {
                Log.e("PRODUCT", "Error fetching product by ID", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Loads the product data into the form for editing.
     * @param product The [ProductResponse] object containing the product data to be edited.
     */
    fun loadProductForEdit(product: ProductResponse) {
        _isLoading.value = true
        _selectedProduct.value = product
        updateProductName(product.name)
        updateProductType(product.productType)
        updateBrand(product.brand)
        updateUnitPrice(product.unitPrice)
        updateCurrencyCode(product.currencyCode)
        updateMinimumStock(product.minimumStock)
        updateProductContent(product.content)
        updateImageFile(null)
        _isLoading.value = false
    }

    /**
     * Clears the product form fields.
     */
    fun clearForm() {
        _selectedProduct.value = null
        _productName.value = ""
        _productType.value = ""
        _brand.value = ""
        _unitPrice.value = 0.0
        _currencyCode.value = ""
        _minimumStock.value = 0
        _content.value = 0.0
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

                val code = _currencyCode.value.trim()
                val type = _productType.value.trim()

                if (code.isBlank()) throw Exception("'Code' field is required")
                if (type.isBlank()) throw Exception("'Type' field is required")

                val productRequest = ProductRequest(
                    name = _productName.value,
                    productType = type,
                    brand = _brand.value,
                    unitPrice = _unitPrice.value,
                    currencyCode = code,
                    minimumStock = _minimumStock.value,
                    content = _content.value,
                    supplierId = null
                )

                Log.d("PRODUCT", "Registering productRequest: $productRequest")

                if (isEditing && productId != null) {
                    repository.updateProduct(
                        productRequest,
                        productId,
                        imageFile
                    )
                } else {
                    repository.registerProduct(
                        productRequest,
                        accountId,
                        imageFile
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

    /**
     * Validates the product content input to ensure it is greater than zero.
     * @return True if the product content input is valid, false otherwise.
     */
    private fun validateProductContent(): Boolean {
        return if (_content.value <= 0) {
            _contentError.value = "Product content must be greater than 0"
            false
        } else {
            _contentError.value = null
            true
        }
    }

    // Fetches all available brands from the repository.
    fun getAllBrands() {
        viewModelScope.launch {
            _brands.value = brandRepository.getAllBrands()
        }
    }

    // Fetches all available product types from the repository
    fun getAllProductTypes() {
        viewModelScope.launch {
            _productTypes.value = typeRepository.getAllProductTypes()
        }
    }

    // Initialize the ViewModel by fetching brands.
    init {
        getAllBrands()
        getAllProductTypes()
    }
}