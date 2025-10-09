package com.liquotrack.stocksip.features.careguides.data.repositories

import com.liquotrack.stocksip.features.careguides.data.remote.services.CareGuideService
import com.liquotrack.stocksip.features.careguides.domain.CareGuide
import com.liquotrack.stocksip.features.careguides.domain.CareGuideRepository
import android.util.Log
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CareGuideRepositoryImpl @Inject constructor(private val service: CareGuideService) : CareGuideRepository {
    
    override suspend fun getById(accountId: String): List<CareGuide> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getCareGuideById(accountId)
                if (response.isSuccessful) {
                    response.body()?.let { careGuideDto ->
                        return@withContext listOf(
                            CareGuide(
                                careGuideId = careGuideDto.id,
                                accountId = careGuideDto.accountId,
                                productId = careGuideDto.productId ?: "",
                                title = careGuideDto.name,
                                summary = careGuideDto.description,
                                recommendedMinTemperature = careGuideDto.recommendedMinTemperature,
                                recommendedMaxTemperature = careGuideDto.recommendedMaxTemperature,
                                recommendedPlaceStorage = careGuideDto.recommendedPlaceStorage,
                                generalRecommendation = careGuideDto.generalRecommendation,
                                guideFileName = null,
                                fileName = null,
                                fileContentType = null,
                                fileData = null
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext emptyList()
        }
    }

    override suspend fun getAllCareGuideBytId(careGuideId: String): CareGuide {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getCareGuideById(careGuideId)
                if (response.isSuccessful) {
                    response.body()?.let { careGuideDto ->
                        return@withContext CareGuide(
                            careGuideId = careGuideDto.id,
                            accountId = careGuideDto.accountId,
                            productId = careGuideDto.productId ?: "",
                            title = careGuideDto.name,
                            summary = careGuideDto.description,
                            recommendedMinTemperature = careGuideDto.recommendedMinTemperature,
                            recommendedMaxTemperature = careGuideDto.recommendedMaxTemperature,
                            recommendedPlaceStorage = careGuideDto.recommendedPlaceStorage,
                            generalRecommendation = careGuideDto.generalRecommendation,
                            guideFileName = null,
                            fileName = null,
                            fileContentType = null,
                            fileData = null
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            throw Exception("CareGuide not found")
        }
    }

    override suspend fun createCareGuide(careGuide: CareGuide): CareGuide {
        TODO("Not yet implemented")
    }

    override suspend fun updateCareGuide(careGuide: CareGuide): CareGuide {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCareGuide(careGuideId: String) {
        TODO("Not yet implemented")
    }
}