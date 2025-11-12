package com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.services

import com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.models.BrandDto
import retrofit2.Response
import retrofit2.http.GET

/**
 * Service interface for fetching brand-related data through remote API calls.
 * This interface defines the endpoints and HTTP methods used to interact with brand-related API.
 */
interface BrandService {

    /**
     * Fetches a list of all available brand names.
     *
     * @return A [Response] containing a [BrandDto] with the list of brand names.
     */
    @GET("brands")
    suspend fun getAllBrands(): Response<BrandDto>
}