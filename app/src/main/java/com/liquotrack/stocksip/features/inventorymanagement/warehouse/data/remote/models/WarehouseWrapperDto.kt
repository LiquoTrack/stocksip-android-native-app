package com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.models


import com.google.gson.annotations.SerializedName

data class WarehouseWrapperDto(
    @SerializedName("total")
    val total: Int,
    @SerializedName("warehouses")
    val warehouses: List<WarehouseDto>
)