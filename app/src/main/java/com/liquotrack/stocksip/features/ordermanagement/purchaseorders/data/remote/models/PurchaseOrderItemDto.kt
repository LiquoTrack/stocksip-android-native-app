package com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.models

import com.google.gson.annotations.SerializedName

data class PurchaseOrderItemDto(
    @SerializedName("productId")
    val productId: String?,
    @SerializedName("productName")
    val productName: String?,
    @SerializedName("unitPrice")
    val unitPrice: Double?,
    @SerializedName("quantity")
    val quantity: Int?,
    @SerializedName("subTotal")
    val subTotal: Double?,
    @SerializedName("imageUrl")
    val imageUrl: String?
)