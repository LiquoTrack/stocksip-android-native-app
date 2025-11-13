package com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing a product type item received from the remote API.
 */
data class ProductTypeDtoItem(
    @SerializedName("name")
    val name: String
)
