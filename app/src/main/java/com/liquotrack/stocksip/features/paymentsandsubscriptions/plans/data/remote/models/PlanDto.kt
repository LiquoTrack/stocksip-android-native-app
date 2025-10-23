package com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.data.remote.models

import com.google.gson.annotations.SerializedName
import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.domain.models.Plan

data class PlanDto(
    @SerializedName("_id")
    val id: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("planType")
    val planType: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("paymentFrequency")
    val paymentFrequency: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("planLimits")
    val planLimits: PlanLimitsDto?
)

// Mapper
fun PlanDto.toDomain(): Plan {
    return Plan(
        id = this.id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        planType = this.planType ?: "Unknown",
        description = this.description,
        paymentFrequency = this.paymentFrequency,
        planPrice = "${this.price ?: 0.0} ${this.currency ?: "USD"}",
        planLimits = this.planLimits?.toDomain()
    )
}