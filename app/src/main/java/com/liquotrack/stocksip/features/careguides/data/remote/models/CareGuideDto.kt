package com.liquotrack.stocksip.features.careguides.data.remote.models

import com.google.gson.annotations.SerializedName

data class CareGuideDto(
    @SerializedName("careGuideId")
    val id: String,
    @SerializedName("title")
    val name: String,
    @SerializedName("summary")
    val description: String,
    @SerializedName("accountId")
    val accountId: String,
    @SerializedName("productId")
    val productId: String?,
    @SerializedName("productAssociated")
    val productAssociated: String?,
    @SerializedName("productName")
    val productName: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("recommendedMinTemperature")
    val recommendedMinTemperature: Double,
    @SerializedName("recommendedMaxTemperature")
    val recommendedMaxTemperature: Double,
    @SerializedName("recommendedPlaceStorage")
    val recommendedPlaceStorage: String,
    @SerializedName("generalRecommendation")
    val generalRecommendation: String,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null
)
