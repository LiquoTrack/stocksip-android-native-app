package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models

data class CatalogItem(
    val productId: String,
    val productName: String,
    val unitPrice: String?,
    val imageUrl: String?,
    val availableStock: Int
)