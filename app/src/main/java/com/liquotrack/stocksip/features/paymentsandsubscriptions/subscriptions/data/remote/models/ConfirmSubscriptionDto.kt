package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.models


import com.google.gson.annotations.SerializedName

/**
 * Data class that represents the confirmation for the payment
 * @param preferenceId - The id of the mercado pago payment process
 * @param status - The status of the subscription process
 */
data class ConfirmSubscriptionDto(
    @SerializedName("preferenceId")
    val preferenceId: String,
    @SerializedName("status")
    val status: String
)