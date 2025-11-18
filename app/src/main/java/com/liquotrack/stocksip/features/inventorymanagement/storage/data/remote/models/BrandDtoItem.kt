package com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.models


import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing a brand item received from the remote API.
 */
data class BrandDtoItem(
    @SerializedName("name")
    val name: String
)