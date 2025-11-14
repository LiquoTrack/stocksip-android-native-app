package com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models

import java.util.Date

/**
 * Data class representing a request to subtract a certain quantity from the inventory,
 *
 * @param quantityToSubtrack The quantity to be subtracted from the inventory.
 * @param expirationDate The expiration date of the product to be subtracted (optional).
 */
data class InventorySubtrackRequest (
    val quantityToSubtrack: Int,
    val expirationDate: Date?
)