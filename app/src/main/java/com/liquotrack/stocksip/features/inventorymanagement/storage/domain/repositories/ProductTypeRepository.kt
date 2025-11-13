package com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories

/**
 * Repository interface for managing Product Type entities.
 * Defines methods for retrieving product type information.
 */
interface ProductTypeRepository {

    /**
     * Retrieves a list of all unique product types.
     *
     * @return A list of product types as strings.
     */
    suspend fun getAllProductTypes(): List<String>
}