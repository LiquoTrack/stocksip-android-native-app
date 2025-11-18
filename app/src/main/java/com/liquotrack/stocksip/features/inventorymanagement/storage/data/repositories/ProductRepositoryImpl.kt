package com.liquotrack.stocksip.features.inventorymanagement.storage.data.repositories

import com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.helpers.toMultipart
import com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.services.ProductService
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductRequest
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductResponse
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductsWithCount
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
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
     * Retrieves all products associated with a specific account ID.
     *
     * @param accountId The unique identifier of the account.
     * @return A list of Product entities associated with the given account ID.
     */
    override suspend fun getAllProductsByAccountId(accountId: String): ProductsWithCount =
        withContext(Dispatchers.IO) {
            try {
                val response = service.getAllProductsByAccountId(accountId)
                if (response.isSuccessful) {
                    response.body()?.let { wrapper ->
                        val products = wrapper.products.map { productDto ->
                            ProductResponse(
                                id = productDto.productId,
                                name = productDto.name,
                                productType = productDto.productType,
                                brand = productDto.brand,
                                unitPrice = productDto.unitPrice,
                                currencyCode = productDto.moneyCode,
                                minimumStock = productDto.minimumStock,
                                content = productDto.content,
                                totalStockInWarehouse = productDto.totalStockInWarehouse,
                                imageUrl = productDto.imageUrl ?: "",
                                supplierId = productDto.supplierId ?: "",
                                isInWarehouse = productDto.isInWarehouse
                            )
                        }
                        return@withContext ProductsWithCount(wrapper.total, wrapper.maxProductsAllowed, products)
                    }
                }
                ProductsWithCount(0, 0, emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
                ProductsWithCount(0, 0, emptyList())
            }
        }

    /**
     * Retrieves all products associated with a specific supplier ID.
     *
     * @param supplierId The unique identifier of the supplier.
     * @return A list of Product entities associated with the given supplier ID.
     */
    override suspend fun getAllProductsBySupplierId(supplierId: String): List<ProductResponse> {
        TODO("Not yet implemented")
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param productId The unique identifier of the product.
     * @return The Product entity if found.
     */
    override suspend fun getProductById(productId: String): ProductResponse = withContext(Dispatchers.IO) {
        try {
            val response = service.getProductById(productId)
            if (response.isSuccessful) {
                response.body()?.let { productDto ->
                    return@withContext ProductResponse(
                        id = productDto.productId,
                        name = productDto.name,
                        productType = productDto.productType,
                        brand = productDto.brand,
                        unitPrice = productDto.unitPrice,
                        currencyCode = productDto.moneyCode,
                        minimumStock = productDto.minimumStock,
                        content = productDto.content,
                        totalStockInWarehouse = productDto.totalStockInWarehouse,
                        imageUrl = productDto.imageUrl ?: "",
                        supplierId = productDto.supplierId ?: "",
                        isInWarehouse = productDto.isInWarehouse
                    )
                }
            } else {
                throw Exception("Error fetching product: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        throw Exception("Failed to fetch product")
    }

    /**
     * Registers a new product.
     *
     * @param product The Product entity to be registered.
     * @param accountId The unique identifier of the account.
     * @param imageFile An optional image file for the product.
     * @return The registered Product entity with its unique identifier.
     */
    override suspend fun registerProduct(
        product: ProductRequest,
        accountId: String,
        imageFile: File?
    ): ProductResponse = withContext(Dispatchers.IO) {
        try {
            val (fields, imagePart) = product.toMultipart(imageFile)

            val response = service.registerProduct(
                accountId = accountId,
                fields = fields,
                image = imagePart
            )

            if (response.isSuccessful) {
                val productDto = response.body()
                if (productDto == null) {
                    val raw = try { response.errorBody()?.string() } catch (_: Exception) { null }
                    throw Exception("Empty response body when registering product (HTTP ${response.code()}). ErrorBody: ${raw ?: "none"}")
                }

                val id = productDto.productId

                if (id.isBlank()) {
                    throw Exception("Response missing productId")
                }

                return@withContext ProductResponse(
                    id = id,
                    name = productDto.name,
                    productType = productDto.productType,
                    brand = productDto.brand,
                    unitPrice = productDto.unitPrice,
                    currencyCode = productDto.moneyCode,
                    minimumStock = productDto.minimumStock,
                    content = productDto.content,
                    totalStockInWarehouse = productDto.totalStockInWarehouse,
                    imageUrl = productDto.imageUrl ?: "",
                    supplierId = productDto.supplierId ?: "",
                    isInWarehouse = productDto.isInWarehouse
                )
            } else {
                val rawError = try { response.errorBody()?.string() } catch (_: Exception) { null }
                throw Exception("Failed to register product: HTTP ${response.code()} ${response.message()} - ErrorBody: ${rawError ?: "none"}")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Failed to register product: ${e.message}", e)
        }
    }

    /**
     * Updates an existing product.
     *
     * @param product The Product entity with updated information.
     * @param productId The unique identifier of the product to be updated.
     * @param imageFile An optional image file for the product.
     * @return The updated Product entity.
     */
    override suspend fun updateProduct(
        product: ProductRequest,
        productId: String,
        imageFile: File?
    ): ProductResponse = withContext(Dispatchers.IO) {
        try {
            val (fields, imagePart) = product.toMultipart(imageFile)

            val response = service.updateProduct(
                productId = productId,
                fields = fields,
                image = imagePart
            )

            if (!response.isSuccessful) {
                throw Exception("Error updating warehouse: ${response.code()} ${response.message()}")
            }

            val body = response.body()

            if (body != null) {
                return@withContext ProductResponse(
                    id = body.productId,
                    name = body.name,
                    productType = body.productType,
                    brand = body.brand,
                    unitPrice = body.unitPrice,
                    currencyCode = body.moneyCode,
                    minimumStock = body.minimumStock,
                    content = body.content,
                    totalStockInWarehouse = body.totalStockInWarehouse,
                    imageUrl = body.imageUrl ?: "",
                    supplierId = body.supplierId ?: "",
                    isInWarehouse = body.isInWarehouse
                )
            } else {
                return@withContext ProductResponse(
                    id = productId,
                    name = product.name,
                    productType = product.productType,
                    brand = product.brand,
                    unitPrice = product.unitPrice,
                    currencyCode = product.currencyCode,
                    minimumStock = product.minimumStock,
                    content = product.content,
                    totalStockInWarehouse = 0,
                    imageUrl = "",
                    supplierId = product.supplierId ?: "",
                    isInWarehouse = false
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        throw Exception("Failed to update product")
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
    ): ProductResponse {
        try {
            val response = service.updateProductMinimumStock(productId, minimumStock)

            if (response.isSuccessful) {
                response.body()?.let { productDto ->
                    return ProductResponse(
                        id = productDto.productId,
                        name = productDto.name,
                        productType = productDto.productType,
                        brand = productDto.brand,
                        unitPrice = productDto.unitPrice,
                        currencyCode = productDto.moneyCode,
                        minimumStock = productDto.minimumStock,
                        content = productDto.content,
                        totalStockInWarehouse = productDto.totalStockInWarehouse,
                        imageUrl = productDto.imageUrl ?: "",
                        supplierId = productDto.supplierId ?: "",
                        isInWarehouse = productDto.isInWarehouse
                    )
                }
            } else {
                throw Exception("Error updating product minimum stock: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        throw Exception("Failed to update product minimum stock")
    }

    /**
     * Deletes a product by its unique identifier.
     *
     * @param productId The unique identifier of the product to be deleted.
     */
    override suspend fun deleteProduct(productId: String) = withContext(Dispatchers.IO) {
        try {
            val response = service.deleteProduct(productId)
            if (!response.isSuccessful) {
                throw Exception("Error deleting product: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Failed to delete product")
        }
    }
}