package com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models

/**
 * Data class representing a Product entity.
 *
 * @param id Unique identifier for the product.
 * @param name Name of the product.
 * @param productType Type or category of the product.
 * @param brand Brand associated with the product.
 * @param unitPrice Price per unit of the product.
 * @param moneyCode Currency code for the unit price (e.g., USD, EUR).
 * @param minimumStock Minimum stock level to maintain for the product.
 * @param totalStockInWarehouse Total stock of the product available in the warehouse.
 * @param imageUrl URL of an image representing the product.
 * @param accountId Identifier for the account associated with the product.
 * @param supplierId Identifier for the supplier of the product.
 * @param isInWarehouse Boolean indicating if the product is currently in the warehouse.
 */
data class Product (
    val id: String,
    val name: String,
    val productType: String,
    val brand: String,
    val unitPrice: Double,
    val currencyCode: String,
    val minimumStock: Int,
    val totalStockInWarehouse: Int,
    val imageUrl: String,
    val accountId: String,
    val supplierId: String,
    val isInWarehouse: Boolean
)