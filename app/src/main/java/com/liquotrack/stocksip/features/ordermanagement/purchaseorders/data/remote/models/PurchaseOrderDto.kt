package com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.models

import com.google.gson.annotations.SerializedName

data class PurchaseOrderDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("orderCode")
    val orderCode: String?,
    @SerializedName("items")
    val items: List<PurchaseOrderItemDto>?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("catalogIdBuyFrom")
    val catalogIdBuyFrom: String?,
    @SerializedName("generationDate")
    val generationDate: String?,
    @SerializedName("buyer")
    val buyer: String?,
    @SerializedName("total")
    val total: Double?,
    @SerializedName("isOrderSent")
    val isOrderSent: Boolean?
)