package com.liquotrack.stocksip.features.ordermanagement.data.remote.models

import com.google.gson.annotations.SerializedName

data class RespondDeliveryProposalRequestDto(
    @SerializedName("accept") val accept: Boolean,
    @SerializedName("notes") val notes: String?
)
