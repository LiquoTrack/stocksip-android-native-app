package com.liquotrack.stocksip.features.inventorymanagement.careguides.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.inventorymanagement.careguides.domain.CareGuide
import com.liquotrack.stocksip.features.inventorymanagement.careguides.domain.CareGuideRepository
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductResponse
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories.ProductRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CareGuideCreateUiState {
    object Idle : CareGuideCreateUiState
    object Loading : CareGuideCreateUiState
    object Success : CareGuideCreateUiState
    data class Error(val message: String) : CareGuideCreateUiState
}

@HiltViewModel
class CareGuideCreateViewModel @Inject constructor(
    private val repository: CareGuideRepository,
    private val tokenManager: TokenManager,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CareGuideCreateUiState>(CareGuideCreateUiState.Idle)
    val uiState: StateFlow<CareGuideCreateUiState> = _uiState.asStateFlow()

    private val _products = MutableStateFlow<List<ProductResponse>>(emptyList())
    val products: StateFlow<List<ProductResponse>> = _products.asStateFlow()

    init {
        loadProducts()
    }

    fun createCareGuide(
        typeOfLiquor: String,
        productName: String,
        title: String,
        summary: String,
        minTemperature: String,
        maxTemperature: String
    ) {
        val minTemp = minTemperature.toDoubleOrNull()
        val maxTemp = maxTemperature.toDoubleOrNull()

        if (
            typeOfLiquor.isBlank() ||
            productName.isBlank() ||
            title.isBlank() ||
            summary.isBlank() ||
            minTemp == null ||
            maxTemp == null
        ) {
            _uiState.value = CareGuideCreateUiState.Error("Complete all fields correctly.")
            return
        }

        viewModelScope.launch {
            _uiState.value = CareGuideCreateUiState.Loading
            try {
                val accountId = tokenManager.getAccountId()
                if (accountId.isNullOrBlank()) {
                    _uiState.value = CareGuideCreateUiState.Error("The user account was not found.")
                    return@launch
                }

                repository.createCareGuide(
                    CareGuide(
                        careGuideId = "",
                        accountId = accountId,
                        productAssociated = typeOfLiquor,
                        productId = "",
                        productName = productName,
                        imageUrl = "",
                        title = title,
                        summary = summary,
                        recommendedMinTemperature = minTemp,
                        recommendedMaxTemperature = maxTemp,
                        recommendedPlaceStorage = "",
                        generalRecommendation = "",
                        guideFileName = null,
                        fileName = null,
                        fileContentType = null,
                        fileData = null
                    )
                )
                _uiState.value = CareGuideCreateUiState.Success
            } catch (e: Exception) {
                _uiState.value = CareGuideCreateUiState.Error(e.message ?: "The guide could not be created.")
            }
        }
    }

    fun consumeState() {
        _uiState.value = CareGuideCreateUiState.Idle
    }

    private fun loadProducts() {
        viewModelScope.launch {
            val accountId = tokenManager.getAccountId()
            if (accountId.isNullOrBlank()) {
                return@launch
            }

            try {
                val data = productRepository.getAllProductsByAccountId(accountId)
                _products.value = data.products
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
