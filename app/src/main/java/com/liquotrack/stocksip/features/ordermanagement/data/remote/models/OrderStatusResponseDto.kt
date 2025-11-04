package com.liquotrack.stocksip.features.ordermanagement.data.remote.models

import com.google.gson.annotations.SerializedName

data class OrderStatusResponseDto(
    @SerializedName("orderId") val orderId: String,
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("lastUpdated") val lastUpdated: String,
    @SerializedName("details") val details: String?
)
