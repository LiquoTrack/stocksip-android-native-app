package com.liquotrack.stocksip.features.careguides.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.careguides.domain.CareGuide
import com.liquotrack.stocksip.features.careguides.domain.CareGuideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

@HiltViewModel
class CareGuideViewModel @Inject constructor(private val repository: CareGuideRepository) : ViewModel(){
    private val _careguides= MutableStateFlow<List<CareGuide>>(emptyList())
    val careGuides: StateFlow<List<CareGuide>> = _careguides

    fun getCareGuideById() {
        viewModelScope.launch {
            try {
                val careGuide = repository.getAllCareGuideBytId("4ebf516d-72b7-4a41-891c-1700fbe0c571")
                _careguides.value = listOf(careGuide)
            } catch (e: Exception) {
                _careguides.value = emptyList()
            }
        }
    }

    init{
        getCareGuideById()
    }
}