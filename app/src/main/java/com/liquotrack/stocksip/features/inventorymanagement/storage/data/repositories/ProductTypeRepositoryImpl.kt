package com.liquotrack.stocksip.features.inventorymanagement.storage.data.repositories

import com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.services.ProductTypeService
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories.ProductTypeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of the [ProductTypeRepository] interface.
 *
 * This repository handles data operations related to product types, including fetching product types
 * from a remote service.
 */
class ProductTypeRepositoryImpl @Inject constructor(private val service: ProductTypeService) : ProductTypeRepository {

    /**
     * Retrieves a list of all unique product types.
     *
     * @return A list of product types as strings.
     */
    override suspend fun getAllProductTypes(): List<String> = withContext(Dispatchers.IO) {
        try {
            val response = service.getAllProductTypes()
            if (response.isSuccessful) {
                val productTypes = response.body() ?: emptyList()
                return@withContext productTypes.map { it.name }.distinct()
            }
            return@withContext emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext emptyList()
        }
    }
}