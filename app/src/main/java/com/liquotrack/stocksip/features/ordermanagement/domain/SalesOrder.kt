package com.liquotrack.stocksip.features.ordermanagement.domain

/**
 * Data class representing a SalesOrder entity.
 *
 * @param orderCode Unique identifier for the sales order.
 * @param purchaseOrderId Account identifier associated with the sales order.
 * @param items Product identifier associated with the sales order.
 * @param status Title of the sales order.
 * @param catalogToBuyFrom Summary of the sales order.
 * @param receiptDate Minimum recommended temperature for the sales order.
 * @param completionDate Maximum recommended temperature for the care guide.
 * @param accountId Recommended storage place for the care guide.
 * @param statusHistory General recommendations for the care guide.
 * @param deliveryProposal DeliveryProposal.
 */
data class SalesOrder(
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
