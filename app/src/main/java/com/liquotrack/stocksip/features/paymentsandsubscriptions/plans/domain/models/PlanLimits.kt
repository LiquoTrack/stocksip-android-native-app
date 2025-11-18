package com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.domain.models

data class PlanLimits(
    val maxWarehouses: Int? = null,
    val maxProducts: Int? = null,
    val maxUsers: Int? = null,
    val storageGuides: Boolean? = null,
    val communitySupport: Boolean? = null,
    val prioritySupport: Boolean? = null,
    val premiumStorageGuides: Boolean? = null
)