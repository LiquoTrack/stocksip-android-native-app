package com.liquotrack.stocksip.features.careguides.domain

data class CareGuideCreate(
    val accountId: String,
    val typeOfLiquor: String,
    val productName: String,
    val title: String,
    val summary: String,
    val recommendedMinTemperature: Double,
    val recommendedMaxTemperature: Double
)
