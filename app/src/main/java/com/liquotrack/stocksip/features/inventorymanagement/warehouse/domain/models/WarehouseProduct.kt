package com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models

data class WarehouseProduct(
    val id: String,
    val name: String,
    val type: String,
    val brand: String,
    val price: Double,
    val currency: String,
    val stock: Int,
    val imageUrl: String?
)