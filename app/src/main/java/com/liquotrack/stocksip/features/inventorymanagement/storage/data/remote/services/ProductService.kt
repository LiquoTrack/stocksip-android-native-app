package com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.services

import com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.models.ProductDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Service interface for managing product data through remote API calls.
 * This interface defines the endpoints and HTTP methods used to interact with product-related API.
 */
interface ProductService {

    /**
     * Fetches all products associated with a specific account ID.
     *
     * @param accountId The ID of the account for which to retrieve products.
     * @return A list of [ProductDto] objects.
     */
    @GET("accounts/{accountId}/products")
    suspend fun getAllProductsByAccountId(@Path("accountId") accountId: String): Response<List<ProductDto>>

    /**
     * Fetches a specific product by its ID.
     *
     * @param productId The ID of the product to retrieve.
     * @return A [ProductDto] object representing the product.
     */
    @GET("products/{productId}")
    suspend fun getProductById(@Path("productId") productId: String): Response<ProductDto>

    /**
     * Registers a new product under a specific account.
     *
     * @param accountId The ID of the account under which to register the product.
     * @param product The [ProductDto] object containing product details to be registered.
     * @return The registered [ProductDto] object.
     */
    @POST("accounts/{accountId}/products")
    suspend fun registerProduct(@Path("accountId") accountId: String, product: ProductDto): Response<ProductDto>

    /**
     * Updates an existing product's details.
     *
     * @param productId The ID of the product to update.
     * @param product The [ProductDto] object containing updated product details.
     * @return The updated [ProductDto] object.
     */
    @PUT("products/{productId}")
    suspend fun updateProduct(@Path("productId") productId: String, product: ProductDto): Response<ProductDto>

    /**
     * Updates the minimum stock level for a specific product.
     *
     * @param productId The ID of the product to update.
     * @param minimumStock The new minimum stock level to be set.
     * @return The updated [ProductDto] object with the new minimum stock level.
     */
    @PATCH("products/{productId}/minimum-stock")
    suspend fun updateProductMinimumStock(@Path("productId") productId: String, minimumStock: Int): Response<ProductDto>

    /**
     * Deletes a specific product by its ID.
     *
     * @param productId The ID of the product to delete.
     */
    @DELETE("products/{productId}")
    suspend fun deleteProduct(@Path("productId") productId: String) : Response<Unit>
}