package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.models


import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for upgrading a subscription plan.
 *
 * @property initPoint The URL to initiate the payment process.
 * @property message A message related to the upgrade process.
 * @property preferenceId The ID of the payment preference.
 */
data class UpgradeSubscriptionResponseDto(
    @SerializedName("initPoint")
    val initPoint: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("preferenceId")
    val preferenceId: String
)