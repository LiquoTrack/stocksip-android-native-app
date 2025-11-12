package com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.models

import com.google.gson.annotations.SerializedName

data class PurchaseOrderRequestDto(
    @SerializedName("orderCode")
    val orderCode: String,
    @SerializedName("catalogIdBuyFrom")
    val catalogIdBuyFrom: String,
    @SerializedName("addressIndex")
    val addressIndex: Int? = null,
)