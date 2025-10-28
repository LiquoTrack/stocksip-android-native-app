package com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models

/**
 * Data class representing a collection of warehouses along with the total count.
 *
 * @property total The total number of warehouses.
 * @property warehouse The list of WarehouseResponse objects.
 */
data class WarehousesWithCount(
    val total: Int,
    val warehouse: List<WarehouseResponse>
)
