package com.liquotrack.stocksip.features.careguides.data.repositories

import com.liquotrack.stocksip.features.careguides.data.remote.services.CareGuideService
import com.liquotrack.stocksip.features.careguides.domain.CareGuide
import com.liquotrack.stocksip.features.careguides.domain.CareGuideRepository
import com.liquotrack.stocksip.features.careguides.data.remote.models.CareGuideCreateDto
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CareGuideRepositoryImpl @Inject constructor(private val service: CareGuideService) : CareGuideRepository {
    
    override suspend fun getById(accountId: String): List<CareGuide> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getCareGuidesByAccountId(accountId)
                if (response.isSuccessful) {
                    response.body()?.let { careGuideDtos ->
                        return@withContext careGuideDtos.map { careGuideDto ->
                            CareGuide(
                                careGuideId = careGuideDto.id,
                                accountId = careGuideDto.accountId,
                                productAssociated = careGuideDto.productAssociated ?: careGuideDto.productId.orEmpty(),
                                productId = careGuideDto.productId.orEmpty(),
                                productName = careGuideDto.productName.orEmpty(),
                                imageUrl = careGuideDto.imageUrl.orEmpty(),
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
                            productAssociated = careGuideDto.productAssociated ?: careGuideDto.productId.orEmpty(),
                            productId = careGuideDto.productId.orEmpty(),
                            productName = careGuideDto.productName.orEmpty(),
                            imageUrl = careGuideDto.imageUrl.orEmpty(),
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
        return withContext(Dispatchers.IO) {
            val request = CareGuideCreateDto(
                typeOfLiquor = careGuide.productAssociated,
                productName = careGuide.productName,
                title = careGuide.title,
                summary = careGuide.summary,
                recommendedMinTemperature = careGuide.recommendedMinTemperature,
                recommendedMaxTemperature = careGuide.recommendedMaxTemperature
            )

            val response = service.createCareGuide(careGuide.accountId, request)
            if (response.isSuccessful) {
                response.body()?.let { createdDto ->
                    return@withContext CareGuide(
                        careGuideId = createdDto.id,
                        accountId = createdDto.accountId,
                        productAssociated = createdDto.productAssociated ?: createdDto.productId.orEmpty(),
                        productId = createdDto.productId.orEmpty(),
                        productName = createdDto.productName.orEmpty(),
                        imageUrl = createdDto.imageUrl.orEmpty(),
                        title = createdDto.name,
                        summary = createdDto.description,
                        recommendedMinTemperature = createdDto.recommendedMinTemperature,
                        recommendedMaxTemperature = createdDto.recommendedMaxTemperature,
                        recommendedPlaceStorage = createdDto.recommendedPlaceStorage,
                        generalRecommendation = createdDto.generalRecommendation,
                        guideFileName = null,
                        fileName = null,
                        fileContentType = null,
                        fileData = null
                    )
                }
                throw IllegalStateException("Empty response body when creating care guide")
            }

            throw HttpException(response)
        }
    }

    override suspend fun updateCareGuide(careGuide: CareGuide): CareGuide {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCareGuide(careGuideId: String) {
        TODO("Not yet implemented")
    }
}