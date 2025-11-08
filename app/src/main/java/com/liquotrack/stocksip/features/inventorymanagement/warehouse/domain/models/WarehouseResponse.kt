package com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models

data class WarehouseResponse(
    val id: String,
    val name: String,
    val street: String,
    val city: String,
    val district: String,
    val postalCode: String,
    val country: String,
    val temperatureMin: Double,
    val temperatureMax: Double,
    val capacity: Double,
    val imageUrl: String
)
