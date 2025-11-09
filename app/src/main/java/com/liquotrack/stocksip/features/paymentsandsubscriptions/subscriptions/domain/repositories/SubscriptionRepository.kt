package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.repositories

import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.models.AccountSubscription
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.models.Subscription

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

    /**
     * Confirms a subscription based on the provided preference ID and status.
     *
     * @param preferenceId The ID of the payment preference.
     * @param status The status of the subscription process.
     * @return A [Boolean] indicating whether the confirmation was successful.
     */
    suspend fun confirmSubscription(preferenceId: String, status: String): Boolean

    /**
     * Fetches the subscription status for the given preference ID.
     *
     * @param preferenceId The ID of the payment preference.
     * @return A [String] representing the current status of the subscription.
     */
    suspend fun fetchSubscriptionStatus(preferenceId: String): String

    /**
     * Fetches the subscription details for the given account ID.
     *
     * @param accountId The ID of the account whose subscription details are to be fetched.
     * @return A [Subscription] object containing the subscription information.
     */
     suspend fun fetchSubscriptionByAccountId(accountId: String): AccountSubscription
}