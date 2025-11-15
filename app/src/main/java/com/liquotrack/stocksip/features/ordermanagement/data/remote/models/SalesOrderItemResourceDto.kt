package com.liquotrack.stocksip.features.ordermanagement.data.remote.models

import com.google.gson.annotations.SerializedName

data class SalesOrderItemResourceDto(
    @SerializedName("productId")
    val productId: String,
    @SerializedName("unitPrice")
    val unitPrice: Double,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("inventoryId")
    val inventoryId: String?,
    @SerializedName("quantityToSell")
    val quantityToSell: Int
)
