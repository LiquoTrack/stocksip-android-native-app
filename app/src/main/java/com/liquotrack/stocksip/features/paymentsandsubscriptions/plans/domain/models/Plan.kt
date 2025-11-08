package com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.domain.models

data class Plan(
    val id: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val planType: String,
    val description: String?,
    val paymentFrequency: String?,
    val planPrice: String,
    val planLimits: PlanLimits?
)