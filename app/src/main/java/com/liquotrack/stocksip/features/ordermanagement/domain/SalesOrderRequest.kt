package com.liquotrack.stocksip.features.ordermanagement.domain

data class GeneratePurchaseOrderRequest(
    val orderCode: String,
    val catalogToBuyFrom: String,
    val receiptDate: String,
    val completitionDate: String,
    val items: List<PurchaseOrderItem>,
    val isAutomatic: Boolean
)
data class PurchaseOrderItem(
    val productId: String,
    val unitPrice: Double,
    val currency: String,
    val inventoryId: String,
    val quantityToSell: Int
)

data class SalesOrderFromProcurementRequest(
    val orderCode: String,
    val purchaseOrderId: String,
    val catalogIdBuyFrom: String,
    val buyerAccountId: String,
    val receiptDate: String,
    val completitionDate: String,
    val items: List<ProcuredItem>
)
data class ProcuredItem(
    val productId: String,
    val quantity: Int,
    val unitPrice: Double
)

data class CreateOrderRequest(
    val orderCode: String,
    val purchaseOrderId: String,
    val items: List<PurchaseOrderItem>,
    val status: String,
    val catalogToBuyFrom: String,
    val receiptDate: String,
    val completitionDate: String
)

data class DeliveryProposeRequest(
    val proposedDate: String,
    val notes: String
)

data class DeliveryRespondRequest(
    val accept: Boolean,
    val notes: String
)

data class UpdateStatusRequest(
    val newStatus: Int,
    val reason: String
)