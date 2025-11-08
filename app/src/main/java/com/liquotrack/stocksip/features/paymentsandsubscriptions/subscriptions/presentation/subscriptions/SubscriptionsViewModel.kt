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
                val subscription = repository.createInitialSubscription(accountId, selectedPlanId)
                _subscriptions.value = subscription
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Confirms a subscription based on the provided preference ID and status.
     *
     * @param preferenceId The ID of the payment preference.
     * @param status The status of the subscription process.
     * @param onResult A callback function that receives a Boolean indicating whether the confirmation was successful.
     */
    fun confirmSubscription(preferenceId: String, status: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {

            try {
                val result = repository.confirmSubscription(preferenceId, status)
                onResult(result)
            } catch (e: Exception) {
                _errorMessage.value = e.message
                onResult(false)
            }
        }
    }

    /**
     * Fetches the subscription status for a given preference ID.
     *
     * @param preferenceId The ID of the payment preference.
     * @param onResult A callback function that receives the subscription status as a String, or null if an error occurred.
     */
    fun fetchSubscriptionStatus(preferenceId: String, onResult: (String?) -> Unit) {
        viewModelScope.launch {

            try {
                val status = repository.fetchSubscriptionStatus(preferenceId)
                onResult(status)
            } catch (e: Exception) {
                _errorMessage.value = e.message
                onResult(null)
            }
        }
    }
}