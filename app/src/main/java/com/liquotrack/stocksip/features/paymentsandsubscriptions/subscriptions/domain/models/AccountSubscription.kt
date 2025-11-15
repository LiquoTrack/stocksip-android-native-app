package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.models

/**
 * Data class representing an account subscription.
 *
 * @param subscriptionId The unique identifier for the subscription.
 * @param planId The identifier for the subscription plan.
 * @param status The current status of the subscription.
 * @param expirationDate The expiration date of the subscription.
 * @param planType The type of the subscription plan.
 * @param paymentFrequency The frequency of payments for the subscription.
 * @param maxUsers The maximum number of users allowed under the subscription.
 * @param maxProducts The maximum number of products allowed under the subscription.
 * @param maxWarehouses The maximum number of warehouses allowed under the subscription.
 */
data class AccountSubscription(
    val subscriptionId: String,
    val planId: String,
    val status: String,
    val expirationDate: String,
    val planType: String,
    val paymentFrequency: String,
    val maxUsers: Int,
    val maxProducts: Int,
    val maxWarehouses: Int,
)
