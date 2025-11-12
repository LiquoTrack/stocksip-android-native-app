package com.liquotrack.stocksip.features.inventorymanagement.inventories.data.remote.models


import com.google.gson.annotations.SerializedName
import java.time.LocalDate

/**
 * Data Transfer Object representing an inventory item received from a remote source.
 */
data class InventoryDtoItem(
    @SerializedName("brand")
    val brand: String,
    @SerializedName("currentState")
    val currentState: String,
    @SerializedName("expirationDate")
    val expirationDate: LocalDate?,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("inventoryId")
    val inventoryId: String,
    @SerializedName("minimumStock")
    val minimumStock: Int,
    @SerializedName("moneyCode")
    val moneyCode: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("productId")
    val productId: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("unitPrice")
    val unitPrice: Double,
    @SerializedName("warehouseId")
    val warehouseId: String
)