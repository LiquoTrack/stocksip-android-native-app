package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.models

import com.google.gson.annotations.SerializedName

data class CatalogDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("catalogItems")
    val catalogItems: List<CatalogItemDto>,

    @SerializedName("ownerAccount")
    val ownerAccount: String,

    @SerializedName("contactEmail")
    val contactEmail: String,

    @SerializedName("isPublished")
    val isPublished: Boolean,

    @SerializedName("warehouseId")
    val warehouseId: String?
)