package com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing a Warehouse.
 * This class is used for serializing and deserializing warehouse data
 * when communicating with remote services.
 */
data class WarehouseDto(
    @SerializedName("warehouseId")
    val id: String,

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
    val capacity: Double,

    @SerializedName("temperatureMin")
    val temperatureMin: Double,

    @SerializedName("temperatureMax")
    val temperatureMax: Double,

    @SerializedName("imageUrl")
    val imageUrl: String
)
