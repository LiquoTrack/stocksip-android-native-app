package com.liquotrack.stocksip.features.ordermanagement.domain

data class SalesOrderItemResource(
    val productId: String,
    val unitPrice: Double,
    val currency: String,
    val inventoryId: String?,
    val quantityToSell: Int
)

data class DeliveryProposalResource(
    val proposedDate: String,
    val notes: String?,
    val status: String,
    val createdAt: String,
    val respondedAt: String?
)

data class SalesOrderResponse(
    val id: String,
    val orderCode: String,
    val purchaseOrderId: String?,
    val items: List<SalesOrderItemResource>,
    val status: String,
    val catalogToBuyFrom: String?,
    val receiptDate: String?,
    val completitionDate: String?,
    val buyer: String?,
    val deliveryProposal: DeliveryProposalResource?,
    val supplierId: String?
)
