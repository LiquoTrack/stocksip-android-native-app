package com.liquotrack.stocksip.features.ordermanagement.data.remote.models

import com.google.gson.annotations.SerializedName

data class LiquorStoreOwnerOrdersResponseDto(
    @SerializedName("liquorStoreOwnerId") val liquorStoreOwnerId: String,
    @SerializedName("orders") val orders: List<SalesOrderDto>,
    @SerializedName("totalOrders") val totalOrders: Int,
    @SerializedName("message") val message: String
)
