package com.liquotrack.stocksip.features.inventorymanagement.storage.data.repositories

import com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.services.ProductService
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.Product
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of the ProductRepository interface.
 *
 * This class provides methods to interact with the product data source,
 * including fetching, registering, updating, and deleting products.
 *
 * @property service The ProductService used to perform network operations.
 */
class ProductRepositoryImpl @Inject constructor(private val service: ProductService): ProductRepository {

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param productId The unique identifier of the product.
     * @return The Product entity if found.
     */
    override suspend fun getProductById(productId: String): Product = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }

    /**
     * Retrieves all products associated with a specific account ID.
     *
     * @param accountId The unique identifier of the account.
     * @return A list of Product entities associated with the given account ID.
     */
    override suspend fun getAllProductsByAccountId(accountId: String): List<Product> =
        withContext(Dispatchers.IO) {
            try {
                val response = service.getAllProductsByAccountId(accountId)
                if (response.isSuccessful) {
                    response.body()?.let { productDtoList ->
                        return@withContext productDtoList.map { productDto ->
                            Product(
                                id = productDto.id,
                                name = productDto.name,
                                productType = productDto.productType,
                                brand = productDto.brand,
                                unitPrice = productDto.unitPrice,
                                currencyCode = productDto.moneyCode,
                                minimumStock = productDto.minimumStock,
                                totalStockInWarehouse = productDto.totalStockInWarehouse,
                                imageUrl = productDto.imageUrl,
                                accountId = productDto.accountId,
                                supplierId = productDto.supplierId,
                                isInWarehouse = productDto.isInWarehouse
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext emptyList()
            }
            return@withContext emptyList()
        }

    /**
     * Retrieves all products associated with a specific supplier ID.
     *
     * @param supplierId The unique identifier of the supplier.
     * @return A list of Product entities associated with the given supplier ID.
     */
    override suspend fun getAllProductsBySupplierId(supplierId: String): List<Product> {
        TODO("Not yet implemented")
    }

    /**
     * Registers a new product.
     *
     * @param product The Product entity to be registered.
     * @return The registered Product entity with its unique identifier.
     */
    override suspend fun registerProduct(product: Product): Product {
        TODO("Not yet implemented")
    }

    /**
     * Updates an existing product.
     *
     * @param product The Product entity with updated information.
     * @return The updated Product entity.
     */
    override suspend fun updateProduct(product: Product): Product {
        TODO("Not yet implemented")
    }

    /**
     * Updates the minimum stock level for a specific product.
     *
     * @param productId The unique identifier of the product.
     * @param minimumStock The new minimum stock level to be set.
     * @return The updated Product entity with the new minimum stock level.
     */
    override suspend fun updateProductMinimumStock(
        productId: String,
        minimumStock: Int
    ): Product {
        TODO("Not yet implemented")
    }

    /**
     * Deletes a product by its unique identifier.
     *
     * @param productId The unique identifier of the product to be deleted.
     */
    override suspend fun deleteProduct(productId: String) {
        TODO("Not yet implemented")
    }
}