package com.liquotrack.stocksip.features.ordermanagement.data.remote.models

import com.google.gson.annotations.SerializedName

data class UpdateOrderStatusRequestDto(
    @SerializedName("newStatus") val newStatus: String,
    @SerializedName("newStatusAlias") val newStatusAlias: String?,
    @SerializedName("reason") val reason: String?
)
