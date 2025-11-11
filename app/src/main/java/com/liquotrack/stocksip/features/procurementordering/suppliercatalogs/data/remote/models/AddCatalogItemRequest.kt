package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.models

import com.google.gson.annotations.SerializedName

data class AddCatalogItemRequest(
    @SerializedName("productId")
    val productId: String,
    @SerializedName("warehouseId")
    val warehouseId: String,
    @SerializedName("stock")
    val stock: Int
)