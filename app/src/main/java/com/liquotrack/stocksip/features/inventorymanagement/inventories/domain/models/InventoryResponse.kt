package com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models

import java.util.Date

/**
 * Data class representing the response for an inventory item with additional info of the related product.
 *
 * @param id The unique identifier of the inventory item.
 * @param productId The unique identifier of the related product.
 * @param name The name of the product.
 * @param type The type/category of the product.
 * @param brand The brand of the product.
 * @param unitPrice The unit price of the product.
 * @param moneyCode The currency code for the unit price.
 * @param minimumStock The minimum stock level for the product.
 * @param imageUrl The URL of the product image.
 * @param currentState The current state of the inventory item (e.g., "In Stock", "Out of Stock").
 * @param quantity The quantity of the product in the inventory.
 * @param warehouseId The unique identifier of the warehouse where the inventory is stored.
 * @param expirationDate The expiration date of the product in the inventory.
 */
data class InventoryResponse (
    val id: String,
    val productId: String,
    val name: String,
    val type: String,
    val brand: String,
    val unitPrice: Double,
    val moneyCode: String,
    val minimumStock: Int,
    val imageUrl: String,
    val currentState: String,
    val quantity: Int,
    val warehouseId: String,
    val expirationDate: Date?
)