package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models

data class Catalog(
    val id: String,
    val name: String,
    val description: String,
    val catalogItems: List<CatalogItem>,
    val ownerAccount: String,
    val contactEmail: String,
    val isPublished: Boolean,
    val warehouseId: String?,
)