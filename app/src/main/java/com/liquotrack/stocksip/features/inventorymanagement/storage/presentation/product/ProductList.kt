package com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.product

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.Product

/**
 * A composable function that displays a list of products using a LazyColumn.
 *
 * @param products The list of products to be displayed.
 */
@Composable
fun ProductList(products: List<Product>) {
    LazyColumn {
        items(products) { product ->
            ProductCard(product = product)
        }
    }
}