package com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.services

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
     * @return A list of brand names as strings.
     */
    @GET("brands")
    suspend fun getAllBrands(): Response<List<String>>
}