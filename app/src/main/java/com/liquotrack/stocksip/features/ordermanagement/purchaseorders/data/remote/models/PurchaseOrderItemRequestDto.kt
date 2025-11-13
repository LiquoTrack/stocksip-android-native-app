package com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.models

import com.google.gson.annotations.SerializedName

data class PurchaseOrderItemRequestDto(
    @SerializedName("productId")
    val productId: String,
    @SerializedName("quantity")
    val quantity: Int
)