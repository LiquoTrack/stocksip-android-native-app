package com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.data.remote.models

import com.google.gson.annotations.SerializedName

data class AccountDto(
    @SerializedName("id") val id: String,
    @SerializedName("businessId") val businessId: String,
    @SerializedName("status") val status: String,
    @SerializedName("role") val role: String,
    @SerializedName("creationDate") val creationDate: String
)
