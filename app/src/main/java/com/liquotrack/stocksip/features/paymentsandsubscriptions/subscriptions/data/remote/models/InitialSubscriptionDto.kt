package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.models


import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing the initial subscription details.
 * @param selectedPlanId The ID of the selected subscription plan.
 */
data class InitialSubscriptionDto(
    @SerializedName("selectedPlanId")
    val selectedPlanId: String
)