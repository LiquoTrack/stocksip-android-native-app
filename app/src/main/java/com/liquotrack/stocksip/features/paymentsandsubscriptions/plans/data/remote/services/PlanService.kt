package com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.data.remote.services

import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.data.remote.models.PlanDto
import retrofit2.Response
import retrofit2.http.GET

interface PlanService {
    @GET("plans")
    suspend fun getAllPlans(): Response<List<PlanDto>>
}