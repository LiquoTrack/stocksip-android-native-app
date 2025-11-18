package com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.data.remote.models

import com.google.gson.annotations.SerializedName
import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.domain.models.PlanLimits

data class PlanLimitsDto(
    @SerializedName("MaxWarehouses")
    val maxWarehouses: Int? = null,
    @SerializedName("MaxProducts")
    val maxProducts: Int? = null,
    @SerializedName("MaxUsers")
    val maxUsers: Int? = null,
    @SerializedName("storageGuides")
    val storageGuides: Boolean? = null,
    @SerializedName("communitySupport")
    val communitySupport: Boolean? = null,
    @SerializedName("prioritySupport")
    val prioritySupport: Boolean? = null,
    @SerializedName("premiumStorageGuides")
    val premiumStorageGuides: Boolean? = null
)

fun PlanLimitsDto.toDomain(): PlanLimits {
    return PlanLimits(
        maxWarehouses = this.maxWarehouses,
        maxProducts = this.maxProducts,
        maxUsers = this.maxUsers,
        storageGuides = this.storageGuides,
        communitySupport = this.communitySupport,
        prioritySupport = this.prioritySupport,
        premiumStorageGuides = this.premiumStorageGuides
    )
}