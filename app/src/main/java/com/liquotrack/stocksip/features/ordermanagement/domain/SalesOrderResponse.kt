package com.liquotrack.stocksip.features.ordermanagement.domain

data class SalesOrderResponse(
    val orderCode: String,
    val purchaseOrderId: String,
    val items: List<String>,
    val status: String,
    val catalogToBuyFrom: String,
    val receiptDate: String,
    val completionDate: String,
    val accountId: String,
    val statusHistory: List<String>,
    val deliveryProposal: String?
)
