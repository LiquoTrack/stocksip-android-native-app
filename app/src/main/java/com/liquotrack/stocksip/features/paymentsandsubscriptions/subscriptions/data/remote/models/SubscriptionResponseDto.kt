package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing the response received after creating a subscription.
 * @param preferenceId The ID of the created subscription preference.
 * @param initPoint The initialization point URL for the subscription process.
 * @param message An optional message related to the subscription creation.
 */
data class SubscriptionResponseDto(
    @SerializedName("preferenceId")
    val preferenceId: String?,
    @SerializedName("initPoint")
    val initPoint: String?,
    @SerializedName("message")
    val message: String?
)

