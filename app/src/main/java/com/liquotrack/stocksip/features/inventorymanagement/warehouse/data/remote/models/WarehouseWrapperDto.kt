package com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.models


import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing a collection of warehouses along with the total count.
 *
 * @property total The total number of warehouses.
 * @property maxWarehousesAllowed The maximum number of warehouses allowed.
 * @property warehouses The list of WarehouseDto objects.
 */
data class WarehouseWrapperDto(
    @SerializedName("total")
    val total: Int,
    @SerializedName("maxWarehousesAllowed")
    val maxWarehousesAllowed: Int,
    @SerializedName("warehouses")
    val warehouses: List<WarehouseDto>
)