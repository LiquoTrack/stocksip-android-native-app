package com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing a Product.
 * This class is used for serializing and deserializing product data
 * when communicating with remote services.
 */
data class ProductDto(
    @SerializedName("product_Id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val productType: String,
    @SerializedName("brand")
    val brand: String,
    @SerializedName("unit_price")
    val unitPrice: Double,
    @SerializedName("code")
    val moneyCode: String,
    @SerializedName("minimum_stock")
    val minimumStock: Int,
    @SerializedName("total_stock_in_warehouse")
    val totalStockInWarehouse: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("account_id")
    val accountId: String,
    @SerializedName("supplier_id")
    val supplierId: String,
    @SerializedName("is_in_warehouse")
    val isInWarehouse: Boolean
)
