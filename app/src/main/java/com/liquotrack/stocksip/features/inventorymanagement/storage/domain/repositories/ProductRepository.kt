package com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories

import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.Product

/**
 * Repository interface for managing Product entities.
 * Defines methods for retrieving, registering, updating, and deleting products.
 */
interface ProductRepository {

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param productId The unique identifier of the product.
     * @return The Product entity if found.
     */
    suspend fun getProductById(productId: String): Product

    /**
     * Retrieves all products associated with a specific account ID.
     *
     * @param accountId The unique identifier of the account.
     * @return A list of Product entities associated with the given account ID.
     */
    suspend fun getAllProductsByAccountId(accountId: String): List<Product>

    /**
     * Retrieves all products associated with a specific supplier ID.
     *
     * @param supplierId The unique identifier of the supplier.
     * @return A list of Product entities associated with the given supplier ID.
     */
    suspend fun getAllProductsBySupplierId(supplierId: String): List<Product>

    /**
     * Registers a new product.
     *
     * @param product The Product entity to be registered.
     * @return The registered Product entity with its unique identifier.
     */
    suspend fun registerProduct(product: Product): Product

    /**
     * Updates an existing product.
     *
     * @param product The Product entity with updated information.
     * @return The updated Product entity.
     */
    suspend fun updateProduct(product: Product): Product

    /**
     * Updates the minimum stock level for a specific product.
     *
     * @param productId The unique identifier of the product.
     * @param minimumStock The new minimum stock level to be set.
     * @return The updated Product entity with the new minimum stock level.
     */
    suspend fun updateProductMinimumStock(productId: String, minimumStock: Int): Product

    /**
     * Deletes a product by its unique identifier.
     *
     * @param productId The unique identifier of the product to be deleted.
     */
    suspend fun deleteProduct(productId: String)
}