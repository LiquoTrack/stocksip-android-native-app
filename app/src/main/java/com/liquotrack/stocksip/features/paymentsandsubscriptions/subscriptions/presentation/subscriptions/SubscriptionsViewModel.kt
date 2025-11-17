package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.presentation.subscriptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.models.AccountSubscription
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

    private val _accountSubscriptions = MutableStateFlow<AccountSubscription?>(null)
    val accountSubscriptions: StateFlow<AccountSubscription?> = _accountSubscriptions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    /**
     * Creates an initial subscription for the user based on the selected plan ID.
     *
     * @param selectedPlanId The ID of the selected subscription plan.
     */
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
     * Fetches the account subscription details for the current user's account.
     */
    fun fetchAccountSubscription() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val accountId = tokenModel.getAccountId() ?: throw Exception("Account ID not found")

            try {
                val subscriptions = repository.fetchSubscriptionByAccountId(accountId)
                _accountSubscriptions.value = subscriptions
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }

    }

    /**
     * Formats the subscription details for display purposes.
     *
     * @param subscription The [AccountSubscription] object to format.
     * @return A formatted [AccountSubscription] object.
     */
    fun formatSubscription(subscription: AccountSubscription): AccountSubscription {
        val formattedDate = when (subscription.expirationDate) {
            "31/12/9999", "12/31/9999" -> "Unlimited"
            else -> subscription.expirationDate
        }

        val formattedStatus =
            if (subscription.status == "PendingUpgradePayment") "Active"
            else subscription.status

        return subscription.copy(
            expirationDate = formattedDate,
            status = formattedStatus
        )
    }

    /**
     * Upgrades the subscription to a new plan.
     *
     * @param subscriptionId The ID of the current subscription.
     * @param newPlanId The ID of the new plan to upgrade to.
     */
    fun upgradeSubscription(subscriptionId: String, newPlanId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val accountId = tokenModel.getAccountId() ?: throw Exception("Account ID not found")

            try {
                val upgradedSubscription = repository.upgradeSubscription(accountId, subscriptionId, newPlanId)
                _subscriptions.value = upgradedSubscription

            } catch (e: Exception) {
                _errorMessage.value = e.message

            } finally {
                _isLoading.value = false
            }
        }
    }

}