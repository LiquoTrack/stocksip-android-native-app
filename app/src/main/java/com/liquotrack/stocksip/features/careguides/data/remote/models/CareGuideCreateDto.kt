package com.liquotrack.stocksip.features.careguides.data.remote.models

import com.google.gson.annotations.SerializedName

data class CareGuideCreateDto(
    @SerializedName("typeOfLiquor")
    val typeOfLiquor: String,
    @SerializedName("productName")
    val productName: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("recommendedMinTemperature")
    val recommendedMinTemperature: Double,
    @SerializedName("recommendedMaxTemperature")
    val recommendedMaxTemperature: Double
)
