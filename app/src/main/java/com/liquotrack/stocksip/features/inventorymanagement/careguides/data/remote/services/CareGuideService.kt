package com.liquotrack.stocksip.features.inventorymanagement.careguides.data.remote.services

import com.liquotrack.stocksip.features.inventorymanagement.careguides.data.remote.models.CareGuideCreateDto
import com.liquotrack.stocksip.features.inventorymanagement.careguides.data.remote.models.CareGuideDto
import com.liquotrack.stocksip.features.inventorymanagement.careguides.data.remote.models.CareGuideUpdateDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CareGuideService {

    @GET("accounts/{accountId}/care-guides")
    suspend fun getCareGuidesByAccountId(@Path("accountId") accountId: String): retrofit2.Response<List<CareGuideDto>>

    @GET("care-guides/{careGuideId}")
    suspend fun getCareGuideById(@Path("careGuideId") careGuideId: String): retrofit2.Response<CareGuideDto>

    @GET("care-guides/{accountId}/{productType}")
    suspend fun getCareGuideByProductType(
        @Path("accountId") accountId: String,
        @Path("productType") productType: String
    ): retrofit2.Response<CareGuideDto>

    @POST("care-guides/{accountId}")
    suspend fun createCareGuide(
        @Path("accountId") accountId: String,
        @Body careGuideDto: CareGuideCreateDto
    ): retrofit2.Response<CareGuideDto>

    @PUT("care-guides/{careGuideId}")
    suspend fun updateCareGuide(
        @Path("careGuideId") careGuideId: String,
        @Body careGuideDto: CareGuideUpdateDto
    ): retrofit2.Response<CareGuideDto>

    @DELETE("care-guides/{careGuideId}")
    suspend fun deleteCareGuide(@Path("careGuideId") careGuideId: String): retrofit2.Response<Unit>

    @PUT("care-guides/{careGuideId}/deallocations")
    suspend fun unassignCareGuide(@Path("careGuideId") careGuideId: String): retrofit2.Response<Unit>

    @PUT("care-guides/{careGuideId}/allocations/{productId}")
    suspend fun assignCareGuide(
        @Path("careGuideId") careGuideId: String,
        @Path("productId") productId: String
    ): retrofit2.Response<Unit>

}