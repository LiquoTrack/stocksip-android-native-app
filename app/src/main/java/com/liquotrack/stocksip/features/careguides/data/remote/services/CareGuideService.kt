package com.liquotrack.stocksip.features.careguides.data.remote.services

import com.liquotrack.stocksip.features.careguides.data.remote.models.CareGuideDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CareGuideService {

    @GET("care-guides/{careGuideId}")
    suspend fun getCareGuideById(@Path("careGuideId") careGuideId: String): retrofit2.Response<CareGuideDto>
}