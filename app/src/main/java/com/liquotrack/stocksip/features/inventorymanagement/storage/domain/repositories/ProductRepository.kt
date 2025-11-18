package com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories

import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductRequest
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductResponse
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductsWithCount
import java.io.File

/**
 * Repository interface for managing Product entities.
 * Defines methods for retrieving, registering, updating, and deleting products.
 */
interface ProductRepository {

    /**
     * Retrieves all products associated with a specific account ID.
     *
     * @param accountId The unique identifier of the account.
     * @return A ProductsWithCount object containing the total count and list of Product entities.
     */
    suspend fun getAllProductsByAccountId(accountId: String): ProductsWithCount

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param productId The unique identifier of the product.
     * @return The Product entity if found, null otherwise.
     */
    suspend fun getProductById(productId: String): ProductResponse

    /**
     * Retrieves all products associated with a specific supplier ID.
     *
     * @param supplierId The unique identifier of the supplier.
     * @return A list of Product entities associated with the given supplier ID.
     */
    suspend fun getAllProductsBySupplierId(supplierId: String): List<ProductResponse>

    /**
     * Registers a new product.
     *
     * @param product The Product entity to be registered.
     * @param accountId The unique identifier of the account.
     * @param imageFile An optional image file for the product.
     * @return The registered Product entity with its unique identifier.
     */
    suspend fun registerProduct(product: ProductRequest, accountId: String, imageFile: File?): ProductResponse

    /**
     * Updates an existing product.
     *
     * @param product The Product entity with updated information.
     * @param productId The unique identifier of the product to be updated.
     * @param imageFile An optional image file for the product.
     * @return The updated Product entity.
     */
    suspend fun updateProduct(product: ProductRequest, productId: String, imageFile: File?): ProductResponse

    /**
     * Updates the minimum stock level for a specific product.
     *
     * @param productId The unique identifier of the product.
     * @param minimumStock The new minimum stock level to be set.
     * @return The updated Product entity with the new minimum stock level.
     */
    suspend fun updateProductMinimumStock(productId: String, minimumStock: Int): ProductResponse

    /**
     * Deletes a product by its unique identifier.
     *
     * @param productId The unique identifier of the product to be deleted.
     */
    suspend fun deleteProduct(productId: String)
}