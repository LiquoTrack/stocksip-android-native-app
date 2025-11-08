package com.liquotrack.stocksip.features.ordermanagement.domain

data class SupplierOrdersResponse(
    val supplierId: String,
    val orders: List<SalesOrderResponse>,
    val totalOrders: Int,
    val message: String
)
