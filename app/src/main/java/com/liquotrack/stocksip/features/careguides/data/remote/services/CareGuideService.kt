package com.liquotrack.stocksip.features.careguides.data.remote.services

import com.liquotrack.stocksip.features.careguides.data.remote.models.CareGuideCreateDto
import com.liquotrack.stocksip.features.careguides.data.remote.models.CareGuideDto
import com.liquotrack.stocksip.features.careguides.data.remote.models.CareGuideUpdateDto
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
    
}