package com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing a Product.
 * This class is used for serializing and deserializing product data
 * when communicating with remote services.
 */
data class ProductDto(
    @SerializedName("productId")
    val productId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val productType: String,
    @SerializedName("brand")
    val brand: String,
    @SerializedName("unitPrice")
    val unitPrice: Double,
    @SerializedName("code")
    val moneyCode: String,
    @SerializedName("minimumStock")
    val minimumStock: Int,
    @SerializedName("totalStockInWarehouse")
    val totalStockInWarehouse: Int,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("supplierId")
    val supplierId: String?,
    @SerializedName("isInWarehouse")
    val isInWarehouse: Boolean
)
