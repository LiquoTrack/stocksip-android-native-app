package com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.data.remote.models

import com.google.gson.annotations.SerializedName
import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.domain.models.Plan
import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.domain.models.PlanLimits

data class PlanDto(
    @SerializedName("planType") val planType: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("paymentFrequency") val paymentFrequency: String?,
    @SerializedName("price") val price: Double?,
    @SerializedName("currency") val currency: String?,
    @SerializedName("maxUsers") val maxUsers: Int?,
    @SerializedName("maxWarehouses") val maxWarehouses: Int?,
    @SerializedName("maxProducts") val maxProducts: Int?
)

fun PlanDto.toDomain(): Plan {
    return Plan(
        id = null,
        createdAt = null,
        updatedAt = null,
        planType = this.planType ?: "Unknown",
        description = this.description,
        paymentFrequency = this.paymentFrequency,
        planPrice = "${this.price ?: 0.0} ${this.currency ?: "USD"}",
        planLimits = PlanLimits(
            maxUsers = this.maxUsers,
            maxWarehouses = this.maxWarehouses,
            maxProducts = this.maxProducts,
            storageGuides = null,
            communitySupport = null,
            prioritySupport = null,
            premiumStorageGuides = null
        )
    )
}