package com.liquotrack.stocksip.features.ordermanagement.data.remote.models

import com.google.gson.annotations.SerializedName

data class CreateSalesOrderItemDto(
    @SerializedName("productId") val productId: String,
    @SerializedName("unitPrice") val unitPrice: Double,
    @SerializedName("currency") val currency: String,
    @SerializedName("inventoryId") val inventoryId: String?,
    @SerializedName("quantityToSell") val quantityToSell: Int
)

data class CreateSalesOrderRequestDto(
    @SerializedName("orderCode") val orderCode: String,
    @SerializedName("purchaseOrderId") val purchaseOrderId: String,
    @SerializedName("items") val items: List<CreateSalesOrderItemDto>,
    @SerializedName("status") val status: String,
    @SerializedName("catalogToBuyFrom") val catalogToBuyFrom: String,
    @SerializedName("receiptDate") val receiptDate: String,
    @SerializedName("completitionDate") val completitionDate: String
)
