package com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.product.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.Product

/**
 * A composable function that displays a list of products using a LazyColumn.
 *
 * @param products The list of products to be displayed.
 */
@Composable
fun ProductList(
    products: List<Product>,
    onClick: () -> Unit
) {
    LazyColumn (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) { items(products) { product ->
            ProductCard(
                product = product,
                onClick = onClick
            )
        }
    }
}