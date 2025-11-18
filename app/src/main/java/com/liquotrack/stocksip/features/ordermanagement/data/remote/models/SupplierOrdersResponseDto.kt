package com.liquotrack.stocksip.features.ordermanagement.data.remote.models

import com.google.gson.annotations.SerializedName

data class SupplierOrdersResponseDto(
    @SerializedName("supplierId") val supplierId: String,
    @SerializedName("orders") val orders: List<SalesOrderDto>,
    @SerializedName("totalOrders") val totalOrders: Int,
    @SerializedName("message") val message: String
)
