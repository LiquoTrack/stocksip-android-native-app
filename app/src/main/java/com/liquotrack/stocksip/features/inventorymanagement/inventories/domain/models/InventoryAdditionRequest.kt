package com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models

import java.util.Date

/**
 * Data class representing a request to add inventory items.
 *
 * @param quantityToAdd The quantity of items to add to the inventory.
 * @param expirationDate The expiration date of the items being added (optional).
 */
data class InventoryAdditionRequest(
    val quantityToAdd: Int,
    val expirationDate: Date?
)