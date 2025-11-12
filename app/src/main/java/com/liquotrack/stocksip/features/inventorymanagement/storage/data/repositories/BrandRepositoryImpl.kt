package com.liquotrack.stocksip.features.inventorymanagement.storage.data.repositories

import com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.services.BrandService
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories.BrandRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of the [BrandRepository] interface.
 *
 * This repository handles data operations related to brands, including fetching brand names
 * from a remote service.
 */
class BrandRepositoryImpl @Inject constructor(private val service: BrandService) : BrandRepository {

    /**
     * Retrieves a list of all unique brand names.
     *
     * @return A list of brand names as strings.
     */
    override suspend fun getAllBrands(): List<String> = withContext(Dispatchers.IO) {
        try {
            val response = service.getAllBrands()
            if (response.isSuccessful) {
                val brands = response.body() ?: emptyList()
                return@withContext brands
            }
            return@withContext emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext emptyList()
        }
    }
}