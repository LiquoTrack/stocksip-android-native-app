package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.models

import com.google.gson.annotations.SerializedName

data class AccountInfoDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("business")
    val business: BusinessInfoDto
)
