package com.liquotrack.stocksip.features.ordermanagement.data.remote.models

import com.google.gson.annotations.SerializedName

data class ProposeDeliveryScheduleRequestDto(
    @SerializedName("proposedDate") val proposedDate: String,
    @SerializedName("notes") val notes: String?
)
