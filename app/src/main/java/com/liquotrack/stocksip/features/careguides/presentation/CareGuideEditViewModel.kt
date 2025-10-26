package com.liquotrack.stocksip.features.careguides.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.careguides.domain.CareGuide
import com.liquotrack.stocksip.features.careguides.domain.CareGuideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CareGuideEditUiState {
    object Loading : CareGuideEditUiState
    data class Loaded(val careGuide: CareGuide) : CareGuideEditUiState
    data class Error(val message: String) : CareGuideEditUiState
    object Deleted : CareGuideEditUiState
}

@HiltViewModel
class CareGuideEditViewModel @Inject constructor(
    private val repository: CareGuideRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CareGuideEditUiState>(CareGuideEditUiState.Loading)
    val uiState: StateFlow<CareGuideEditUiState> = _uiState.asStateFlow()

    fun loadCareGuide(careGuideId: String) {
        viewModelScope.launch {
            _uiState.value = CareGuideEditUiState.Loading
            try {
                val careGuide = repository.getAllCareGuideBytId(careGuideId)
                _uiState.value = CareGuideEditUiState.Loaded(careGuide)
            } catch (e: Exception) {
                _uiState.value = CareGuideEditUiState.Error(e.message ?: "The guide could not be loaded.")
            }
        }
    }

    fun deleteCareGuide(careGuideId: String) {
        viewModelScope.launch {
            try {
                repository.deleteCareGuide(careGuideId)
                _uiState.value = CareGuideEditUiState.Deleted
            } catch (e: NotImplementedError) {
                _uiState.value = CareGuideEditUiState.Error("Delete is not available yet.")
            } catch (e: Exception) {
                _uiState.value = CareGuideEditUiState.Error(e.message ?: "The guide could not be deleted.")
            }
        }
    }

    fun updateLocalCareGuide(careGuide: CareGuide) {
        _uiState.value = CareGuideEditUiState.Loaded(careGuide)
    }

    suspend fun updateCareGuide(careGuide: CareGuide): Boolean {
        return try {
            val updated = repository.updateCareGuide(careGuide)
            _uiState.value = CareGuideEditUiState.Loaded(updated)
            true
        } catch (e: NotImplementedError) {
            _uiState.value = CareGuideEditUiState.Error("Update is not available yet.")
            false
        } catch (e: Exception) {
            _uiState.value = CareGuideEditUiState.Error(e.message ?: "The guide could not be updated.")
            false
        }
    }

    suspend fun deleteCareGuideSuspending(careGuideId: String): Boolean {
        return try {
            repository.deleteCareGuide(careGuideId)
            _uiState.value = CareGuideEditUiState.Deleted
            true
        } catch (e: NotImplementedError) {
            _uiState.value = CareGuideEditUiState.Error("Delete is not available yet.")
            false
        } catch (e: Exception) {
            _uiState.value = CareGuideEditUiState.Error(e.message ?: "The guide could not be deleted.")
            false
        }
    }
}
