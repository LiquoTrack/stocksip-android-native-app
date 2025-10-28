package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.models


import com.google.gson.annotations.SerializedName

data class SubscriptionStatusDto(
    @SerializedName("subscriptionStatus")
    val subscriptionStatus: String
)