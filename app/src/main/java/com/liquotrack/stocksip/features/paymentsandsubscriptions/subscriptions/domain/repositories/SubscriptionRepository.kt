package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.repositories

import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.models.Subscription
import retrofit2.Response

/**
 * Repository interface for managing subscriptions.
 */
interface SubscriptionRepository {

    /**
     * Creates an initial subscription for the given account and selected plan.
     *
     * @param accountId The ID of the account for which the subscription is to be created.
     * @param selectedPlanId The ID of the selected subscription plan.
     * @return A [Subscription] object representing the created subscription.
     */
    suspend fun createInitialSubscription(accountId: String, selectedPlanId: String): Subscription
}