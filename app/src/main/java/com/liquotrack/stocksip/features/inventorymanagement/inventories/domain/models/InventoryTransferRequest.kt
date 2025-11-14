package com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models

import java.util.Date

/**
 * Data class representing a request to transfer a certain quantity of inventory to a different warehouse.
 */
data class InventoryTransferRequest(
    val destinationWarehouseId: String,
    val quantityToTransfer: Int,
    val expirationDate: Date?
)
