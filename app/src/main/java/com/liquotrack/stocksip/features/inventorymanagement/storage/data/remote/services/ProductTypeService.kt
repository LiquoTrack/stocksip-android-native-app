package com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.services

import com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.models.ProductTypeDto
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductTypeResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Service interface for fetching product types from the remote API.
 */
interface ProductTypeService {

    /**
     * Fetches all available product types from the remote API.
     *
     * @return A [Response] containing a [ProductTypeDto] with the list of product types.
     */
    @GET("product-types")
    suspend fun getAllProductTypes(): Response<ProductTypeDto>
}