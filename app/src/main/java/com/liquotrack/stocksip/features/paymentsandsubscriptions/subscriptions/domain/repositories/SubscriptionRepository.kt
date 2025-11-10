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
     * Fetches the subscription details for the given account ID.
     *
     * @param accountId The ID of the account whose subscription details are to be fetched.
     * @return A [Subscription] object containing the subscription information.
     */
     suspend fun fetchSubscriptionByAccountId(accountId: String): AccountSubscription

    /**
     * Upgrades the subscription for the given account to a new plan.
     *
     * @param accountId The ID of the account whose subscription is to be upgraded.
     * @param subscriptionId The ID of the subscription to be upgraded.
     * @param newPlanId The ID of the new subscription plan.
     * @return A [Subscription] object representing the upgraded subscription.
     */
    suspend fun upgradeSubscription(accountId: String, subscriptionId: String, newPlanId: String): Subscription
}