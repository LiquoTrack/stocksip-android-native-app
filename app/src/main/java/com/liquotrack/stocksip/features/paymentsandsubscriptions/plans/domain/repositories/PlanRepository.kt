package com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.domain.repositories

import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.domain.models.Plan

interface PlanRepository {
    suspend fun getAllPlans(): List<Plan>
}