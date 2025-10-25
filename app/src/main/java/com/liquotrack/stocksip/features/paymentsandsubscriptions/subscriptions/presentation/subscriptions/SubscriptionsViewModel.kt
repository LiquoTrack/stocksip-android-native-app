package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.presentation.subscriptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.models.Subscription
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.repositories.SubscriptionRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    private val repository: SubscriptionRepository,
    private val tokenModel: TokenManager
) : ViewModel() {

    private val _subscriptions = MutableStateFlow<Subscription?>(null)
    val subscriptions: StateFlow<Subscription?> = _subscriptions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun createInitialSubscription(selectedPlanId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val accountId = tokenModel.getAccountId() ?: throw Exception("Account ID not found")

            try {
                println("🟡 Creando suscripción para accountId=$accountId con planId=$selectedPlanId")
                val subscription = repository.createInitialSubscription(accountId, selectedPlanId)
                println("✅ Suscripción creada correctamente: ${subscription.initPoint}")
                _subscriptions.value = subscription
            } catch (e: Exception) {
                println("❌ Error al crear suscripción: ${e.message}")
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
                println("🔹 Finalizó proceso de creación de suscripción")
            }
        }
    }


    fun clearSubscription() {
        _subscriptions.value = null
    }
}