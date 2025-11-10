package com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models

/**
 * Data class representing a request to create or update a product in the inventory management system.
 *
 * @property name The name of the product.
 * @property productType The type or category of the product.
 * @property brand The brand associated with the product.
 * @property unitPrice The price per unit of the product.
 * @property currencyCode The currency code for the unit price (e.g., USD, EUR).
 * @property minimumStock The minimum stock level for the product.
 */
data class ProductRequest(
    val name: String,
    val productType: String,
    val brand: String,
    val unitPrice: Double,
    val currencyCode: String,
    val minimumStock: Int
)
