package com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing a collection of products along with the total count.
 *
 * @property total The total number of products.
 * @property maxProductsAllowed The maximum number of products allowed.
 * @property products The list of ProductDto objects.
 */
data class ProductWrapperDto(
    @SerializedName("total")
    val total: Int,
    @SerializedName("maxProductsAllowed")
    val maxProductsAllowed: Int,
    @SerializedName("products")
    val products: List<ProductDto>
)
