package com.liquotrack.stocksip.features.careguides.data.remote.models

import com.google.gson.annotations.SerializedName

data class CareGuideUpdateDto(
    @SerializedName("careGuideId")
    val careGuideId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("recommendedMinTemperature")
    val recommendedMinTemperature: Double,
    @SerializedName("recommendedMaxTemperature")
    val recommendedMaxTemperature: Double,
    @SerializedName("recommendedPlaceStorage")
    val recommendedPlaceStorage: String,
    @SerializedName("generalRecommendation")
    val generalRecommendation: String
)
