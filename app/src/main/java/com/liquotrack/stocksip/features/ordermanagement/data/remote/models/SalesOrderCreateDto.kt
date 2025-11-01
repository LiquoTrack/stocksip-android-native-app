package com.liquotrack.stocksip.features.ordermanagement.data.remote.models

import com.google.gson.annotations.SerializedName

data class SalesOrderCreateDto(
    @SerializedName("orderCode")
    val orderCode: String,
    @SerializedName("purchaseOrderId")
    val purchaseOrderId: String,
    @SerializedName("items")
    val items: List<String>,
    @SerializedName("status")
    val status: String,
    @SerializedName("catalogToBuyFrom")
    val catalogToBuyFrom: String,
    @SerializedName("receiptDate")
    val receiptDate: String,
    @SerializedName("completionDate")
    val completionDate: String,
    @SerializedName("accountId")
    val accountId: String,
    @SerializedName("statusHistory")
    val statusHistory: List<String>,
    @SerializedName("deliveryProposal")
    val deliveryProposal: String?
)
