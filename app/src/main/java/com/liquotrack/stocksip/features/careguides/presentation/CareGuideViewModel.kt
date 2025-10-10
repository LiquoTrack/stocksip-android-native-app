package com.liquotrack.stocksip.features.careguides.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.careguides.domain.CareGuide
import com.liquotrack.stocksip.features.careguides.domain.CareGuideRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CareGuideViewModel @Inject constructor(
    private val repository: CareGuideRepository,
    private val tokenManager: TokenManager
) : ViewModel(){
    private val _careguides = MutableStateFlow<List<CareGuide>>(emptyList())
    val careGuides: StateFlow<List<CareGuide>> = _careguides

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

    init {
        loadCareGuides()
    }
}