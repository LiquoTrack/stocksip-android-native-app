package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.models

import com.google.gson.annotations.SerializedName

data class CatalogItemDto(
    @SerializedName("productId")
    val productId: String,

    @SerializedName("productName")
    val productName: String,

    @SerializedName("amount")
    val unitPrice: String?,

    @SerializedName("productImage")
    val imageUrl: String?,

    @SerializedName("availableStock")
    val availableStock: Int? = 0
)

data class MongoId(
    @SerializedName("\$oid")
    val oid: String
)