package com.liquotrack.stocksip.features.ordermanagement.domain

data class OrderStatusResponse(
    val orderId: String,
    val status: String,
    val message: String,
    val lastUpdated: String,
    val details: String?
)
