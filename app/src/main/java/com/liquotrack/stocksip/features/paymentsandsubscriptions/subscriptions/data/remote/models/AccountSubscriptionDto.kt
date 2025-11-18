package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.models


import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing an account subscription.
 *
 * @property expirationDate The expiration date of the subscription.
 * @property maxProducts The maximum number of products allowed under the subscription.
 * @property maxUsers The maximum number of users allowed under the subscription.
 * @property maxWarehouses The maximum number of warehouses allowed under the subscription.
 * @property paymentFrequency The frequency of payments for the subscription.
 * @property planId The identifier for the subscription plan.
 * @property planType The type of the subscription plan.
 * @property status The current status of the subscription.
 * @property subscriptionId The unique identifier for the subscription.
 */
data class AccountSubscriptionDto(
    @SerializedName("expirationDate")
    val expirationDate: String,
    @SerializedName("maxProducts")
    val maxProducts: Int,
    @SerializedName("maxUsers")
    val maxUsers: Int,
    @SerializedName("maxWarehouses")
    val maxWarehouses: Int,
    @SerializedName("paymentFrequency")
    val paymentFrequency: String,
    @SerializedName("planId")
    val planId: String,
    @SerializedName("planType")
    val planType: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("subscriptionId")
    val subscriptionId: String
)