package com.liquotrack.stocksip.features.ordermanagement.domain

data class LiquorStoreOwnerOrdersResponse(
    val liquorStoreOwnerId: String,
    val orders: List<SalesOrderResponse>,
    val totalOrders: Int,
    val message: String
)
