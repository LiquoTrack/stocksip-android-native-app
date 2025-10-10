package com.liquotrack.stocksip.features.careguides.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.careguides.domain.CareGuide
import com.liquotrack.stocksip.features.careguides.domain.CareGuideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val DEFAULT_ACCOUNT_ID = "68e8503bb04c5f93ede2cb6f"

sealed interface CareGuideCreateUiState {
    object Idle : CareGuideCreateUiState
    object Loading : CareGuideCreateUiState
    object Success : CareGuideCreateUiState
    data class Error(val message: String) : CareGuideCreateUiState
}

@HiltViewModel
class CareGuideCreateViewModel @Inject constructor(
    private val repository: CareGuideRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CareGuideCreateUiState>(CareGuideCreateUiState.Idle)
    val uiState: StateFlow<CareGuideCreateUiState> = _uiState.asStateFlow()

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
            _uiState.value = CareGuideCreateUiState.Error("Completa todos los campos correctamente.")
            return
        }

        viewModelScope.launch {
            _uiState.value = CareGuideCreateUiState.Loading
            try {
                repository.createCareGuide(
                    CareGuide(
                        careGuideId = "",
                        accountId = DEFAULT_ACCOUNT_ID,
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
                _uiState.value = CareGuideCreateUiState.Error(e.message ?: "No se pudo crear la guía.")
            }
        }
    }

    fun consumeState() {
        _uiState.value = CareGuideCreateUiState.Idle
    }
}
