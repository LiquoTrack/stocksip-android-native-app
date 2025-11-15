package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.models

import com.google.gson.annotations.SerializedName

data class BusinessInfoDto(
    @SerializedName("businessName")
    val businessName: String,

    @SerializedName("businessEmail")
    val businessEmail: String,

    @SerializedName("ruc")
    val ruc: String
)