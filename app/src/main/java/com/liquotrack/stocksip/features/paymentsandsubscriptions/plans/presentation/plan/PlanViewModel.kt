package com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.presentation.plan

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.domain.models.Plan
import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.domain.repositories.PlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing Plan data and operations.
 * This ViewModel interacts with the PlanRepository to fetch plan information.
 */
@HiltViewModel
class PlanViewModel @Inject constructor(
    private val repository: PlanRepository
) : ViewModel() {

    private val _plans = MutableStateFlow<List<Plan>>(emptyList())
    val plans: StateFlow<List<Plan>> = _plans.asStateFlow()

    private val _selectedPlan = MutableStateFlow<Plan?>(null)
    val selectedPlan: StateFlow<Plan?> = _selectedPlan.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun selectPlan(plan: Plan) {
        _selectedPlan.value = plan
        Log.d("PLAN", "Plan selected: ${plan.planType}")
    }

    fun clearSelection() {
        _selectedPlan.value = null
    }

    fun getAllPlans() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                _plans.value = repository.getAllPlans()
                Log.d("PLAN", "Received: ${_plans.value.size} plans")
            } catch (e: Exception) {
                Log.e("PLAN", "Error loading plans", e)
                _errorMessage.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    init {
        getAllPlans()
    }
}