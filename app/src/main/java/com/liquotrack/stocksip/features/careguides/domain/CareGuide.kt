package com.liquotrack.stocksip.features.careguides.domain


/**
 * Data class representing a CareGuide entity.
 *
 * @param careGuideId Unique identifier for the care guide.
 * @param accountId Account identifier associated with the care guide.
 * @param productId Product identifier associated with the care guide.
 * @param title Title of the care guide.
 * @param summary Summary of the care guide.
 * @param recommendedMinTemperature Minimum recommended temperature for the care guide.
 * @param recommendedMaxTemperature Maximum recommended temperature for the care guide.
 * @param recommendedPlaceStorage Recommended storage place for the care guide.
 * @param generalRecommendation General recommendations for the care guide.
 * @param guideFileName Name of the guide file.
 * @param fileName Name of the file.
 * @param fileContentType Content type of the file.
 * @param fileData Data of the file.
 */
data class CareGuide(
    val careGuideId: String,
    val accountId: String,
    val productAssociated: String,
    val productId: String,
    val productName: String,
    val imageUrl: String,
    val title: String,
    val summary: String,
    val recommendedMinTemperature: Double,
    val recommendedMaxTemperature: Double,
    val recommendedPlaceStorage: String,
    val generalRecommendation: String,
    val guideFileName: String?,
    val fileName: String?,
    val fileContentType: String?,
    val fileData: ByteArray?
)