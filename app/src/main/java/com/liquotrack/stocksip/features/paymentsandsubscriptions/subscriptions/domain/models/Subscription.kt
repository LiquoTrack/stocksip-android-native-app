package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.models

/**
 * Domain model representing a subscription.
 * @param accountId The ID of the account associated with the subscription.
 * @param planId The ID of the subscription plan.
 * @param preferenceId The ID of the created subscription preference.
 * @param initPoint The initialization point URL for the subscription process.
 * @param message An optional message related to the subscription creation.
 */
data class Subscription(
    val accountId: String,
    val planId: String,
    val preferenceId: String?,
    val initPoint: String?,
    val message: String?,
    var isPaymentLaunched: Boolean = false
)
