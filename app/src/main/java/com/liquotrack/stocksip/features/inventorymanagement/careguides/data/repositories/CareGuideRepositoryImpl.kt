package com.liquotrack.stocksip.features.inventorymanagement.careguides.data.repositories

import com.liquotrack.stocksip.features.inventorymanagement.careguides.data.remote.models.CareGuideDto
import com.liquotrack.stocksip.features.inventorymanagement.careguides.data.remote.services.CareGuideService
import com.liquotrack.stocksip.features.inventorymanagement.careguides.domain.CareGuide
import com.liquotrack.stocksip.features.inventorymanagement.careguides.domain.CareGuideRepository

import com.liquotrack.stocksip.features.inventorymanagement.careguides.data.remote.models.CareGuideCreateDto
import com.liquotrack.stocksip.features.inventorymanagement.careguides.data.remote.models.CareGuideUpdateDto
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
                        return@withContext careGuideDtos.map { it.toDomain() }
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
                        return@withContext careGuideDto.toDomain()
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
                    return@withContext createdDto.toDomain()
                }
                throw IllegalStateException("Empty response body when creating care guide")
            }

            throw HttpException(response)
        }
    }

    override suspend fun updateCareGuide(careGuide: CareGuide): CareGuide {
        return withContext(Dispatchers.IO) {
            val request = CareGuideUpdateDto(
                careGuideId = careGuide.careGuideId,
                title = careGuide.title,
                summary = careGuide.summary,
                recommendedMinTemperature = careGuide.recommendedMinTemperature,
                recommendedMaxTemperature = careGuide.recommendedMaxTemperature,
                recommendedPlaceStorage = careGuide.recommendedPlaceStorage,
                generalRecommendation = careGuide.generalRecommendation
            )

            val response = service.updateCareGuide(careGuide.careGuideId, request)
            if (response.isSuccessful) {
                response.body()?.let { updatedDto ->
                    return@withContext updatedDto.toDomain()
                }
                throw IllegalStateException("Empty response body when updating care guide")
            }

            throw HttpException(response)
        }
    }

    override suspend fun deleteCareGuide(careGuideId: String) {
        return withContext(Dispatchers.IO) {
            val response = service.deleteCareGuide(careGuideId)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        }
    }

    override suspend fun getCareGuideByProductType(accountId: String, productType: String): CareGuide {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getCareGuideByProductType(accountId, productType)
                if (response.isSuccessful) {
                    response.body()?.let { careGuideDto ->
                        return@withContext careGuideDto.toDomain()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            throw Exception("CareGuide not found for product type $productType")
        }
    }

    override suspend fun unassignCareGuide(careGuideId: String) {
        return withContext(Dispatchers.IO) {
            val response = service.unassignCareGuide(careGuideId)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        }
    }

    override suspend fun assignCareGuide(careGuideId: String, productId: String) {
        return withContext(Dispatchers.IO) {
            val response = service.assignCareGuide(careGuideId, productId)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        }
    }
}

private fun CareGuideDto.toDomain(): CareGuide {
    return CareGuide(
        careGuideId = id,
        accountId = accountId,
        productAssociated = productAssociated ?: productId.orEmpty(),
        productId = productId.orEmpty(),
        productName = productName.orEmpty(),
        imageUrl = imageUrl.orEmpty(),
        title = name,
        summary = description,
        recommendedMinTemperature = recommendedMinTemperature,
        recommendedMaxTemperature = recommendedMaxTemperature,
        recommendedPlaceStorage = recommendedPlaceStorage,
        generalRecommendation = generalRecommendation,
        guideFileName = null,
        fileName = null,
        fileContentType = null,
        fileData = null
    )
}