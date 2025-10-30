package com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models

/**
 * Data class representing a collection of warehouses along with the total count.
 *
 * @property total The total number of warehouses.
 * @property maxWarehousesAllowed The maximum number of warehouses allowed.
 * @property warehouses The list of WarehouseResponse objects.
 */
data class WarehousesWithCount(
    val total: Int,
    val maxWarehousesAllowed: Int,
    val warehouses: List<WarehouseResponse>
)
