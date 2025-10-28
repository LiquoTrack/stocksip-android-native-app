package com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.models


import com.google.gson.annotations.SerializedName

data class WarehouseDto(
    @SerializedName("warehouseId")
    val warehouseId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("addressStreet")
    val addressStreet: String,
    @SerializedName("addressCity")
    val addressCity: String,
    @SerializedName("addressDistrict")
    val addressDistrict: String,
    @SerializedName("addressPostalCode")
    val addressPostalCode: String,
    @SerializedName("addressCountry")
    val addressCountry: String,
    @SerializedName("capacity")
    val capacity: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("temperatureMax")
    val temperatureMax: Int,
    @SerializedName("temperatureMin")
    val temperatureMin: Int
)