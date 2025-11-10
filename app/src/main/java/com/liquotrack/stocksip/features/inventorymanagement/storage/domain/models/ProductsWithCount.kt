package com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models

/**
 * Data class representing a collection of products along with the total count.
 *
 * @property total The total number of products.
 * @property maxProductsAllowed The maximum number of products allowed.
 * @property products The list of ProductResponse objects.
 */
data class ProductsWithCount(
    val total: Int,
    val maxProductsAllowed: Int,
    val products: List<ProductResponse>
)
