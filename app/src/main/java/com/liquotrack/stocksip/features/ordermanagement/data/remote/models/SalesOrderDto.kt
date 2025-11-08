package com.liquotrack.stocksip.features.ordermanagement.data.remote.models

import com.google.gson.annotations.SerializedName

data class SalesOrderItemDto(
    @SerializedName("productId") val productId: String,
    @SerializedName("unitPrice") val unitPrice: Double,
    @SerializedName("currency") val currency: String,
    @SerializedName("inventoryId") val inventoryId: String?,
    @SerializedName("quantityToSell") val quantityToSell: Int
)

data class DeliveryProposalDto(
    @SerializedName("proposedDate") val proposedDate: String,
    @SerializedName("notes") val notes: String?,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("respondedAt") val respondedAt: String?
)

data class SalesOrderDto(
    @SerializedName("id") val id: String,
    @SerializedName("orderCode") val orderCode: String,
    @SerializedName("purchaseOrderId") val purchaseOrderId: String,
    @SerializedName("items") val items: List<SalesOrderItemDto>,
    @SerializedName("status") val status: String,
    @SerializedName("catalogToBuyFrom") val catalogToBuyFrom: String,
    @SerializedName("receiptDate") val receiptDate: String,
    @SerializedName("completitionDate") val completitionDate: String,
    @SerializedName("buyer") val buyer: String,
    @SerializedName("deliveryProposal") val deliveryProposal: DeliveryProposalDto?
)