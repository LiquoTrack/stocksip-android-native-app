package com.liquotrack.stocksip.features.inventorymanagement.careguides.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductResponse
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories.ProductRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager

import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CareGuideViewModel @Inject constructor(
    private val repository: CareGuideRepository,
    private val productRepository: ProductRepository,
    private val tokenManager: TokenManager
) : ViewModel(){
    private val _careguides = MutableStateFlow<List<CareGuide>>(emptyList())
    val careGuides: StateFlow<List<CareGuide>> = _careguides

    private val _products = MutableStateFlow<List<ProductResponse>>(emptyList())
    val products: StateFlow<List<ProductResponse>> = _products

    fun loadCareGuides() {
        viewModelScope.launch {
            val accountId = tokenManager.getAccountId()
            if (accountId.isNullOrBlank()) {

                _careguides.value = emptyList()
                return@launch
            }

            try {
                _careguides.value = repository.getById(accountId)
            } catch (e: Exception) {
                _careguides.value = emptyList()
            }
        }
    }

    private var productsLoaded = false

    fun loadProducts(forceRefresh: Boolean = false) {
        if (productsLoaded && !forceRefresh) return
        viewModelScope.launch {
            val accountId = tokenManager.getAccountId()
            if (accountId.isNullOrBlank()) {
                _products.value = emptyList()
                return@launch
            }

            try {
                _products.value = productRepository.getAllProductsByAccountId(accountId).products
                productsLoaded = true
            } catch (e: Exception) {
                _products.value = emptyList()
            }
        }
    }

    init {
        loadCareGuides()
        loadProducts(forceRefresh = true)
    }

    suspend fun assignCareGuide(careGuideId: String, productId: String): Boolean {
        return try {
            repository.assignCareGuide(careGuideId, productId)

            loadCareGuides()
            true
        } catch (e: Exception) {
            false
        }
    }
}