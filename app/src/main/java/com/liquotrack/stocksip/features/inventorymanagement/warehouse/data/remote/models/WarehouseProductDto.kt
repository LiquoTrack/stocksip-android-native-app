package com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.models

import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseProduct

data class WarehouseProductDto(
    val inventoryId: String,
    val productId: String,
    val name: String,
    val type: String,
    val brand: String,
    val unitPrice: Double,
    val moneyCode: String,
    val minimumStock: Int,
    val imageUrl: String?,
    val currentState: String,
    val quantity: Int,
    val warehouseId: String,
    val accountId: String,
    val expirationDate: String?
)

fun WarehouseProductDto.toDomain(): WarehouseProduct {
    return WarehouseProduct(
        id = productId,
        name = name,
        type = type,
        brand = brand,
        price = unitPrice,
        currency = moneyCode,
        stock = quantity,
        imageUrl = imageUrl
    )
}