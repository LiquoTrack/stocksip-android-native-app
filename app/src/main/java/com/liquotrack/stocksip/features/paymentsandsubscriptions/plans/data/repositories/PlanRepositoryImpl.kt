package com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.data.repositories

import android.util.Log
import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.data.remote.models.toDomain
import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.data.remote.services.PlanService
import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.domain.models.Plan
import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.domain.repositories.PlanRepository
import javax.inject.Inject

class PlanRepositoryImpl @Inject constructor(
    private val apiService: PlanService
) : PlanRepository {

    override suspend fun getAllPlans(): List<Plan> {
        try {
            val response = apiService.getAllPlans()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.d("PLAN", "Plans fetched successfully: ${body.size} plans")
                    return body.map { it.toDomain() }
                } else {
                    Log.e("PLAN", "Response body is null")
                    throw Exception("Response body is null")
                }
            } else {
                Log.e("PLAN", "Error fetching plans: ${response.code()} - ${response.message()}")
                throw Exception("Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("PLAN", "Exception fetching plans", e)
            throw e
        }
    }
}