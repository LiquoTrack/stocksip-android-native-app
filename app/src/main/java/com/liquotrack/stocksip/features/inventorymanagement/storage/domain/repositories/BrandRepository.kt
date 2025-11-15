package com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories

/**
 * Repository interface for managing Brand entities.
 * Defines methods for retrieving brand information.
 */
interface BrandRepository {

    /**
     * Retrieves a list of all unique brand names.
     *
     * @return A list of brand names as strings.
     */
    suspend fun getAllBrands(): List<String>
}