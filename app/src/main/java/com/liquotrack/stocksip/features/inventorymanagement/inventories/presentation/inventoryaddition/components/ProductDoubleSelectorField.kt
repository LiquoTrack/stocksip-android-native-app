package com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventoryaddition.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryResponse
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductResponse

/**
 * A composable function that displays two side-by-side selectors: one for products in inventory and another for all products.
 *
 * @param products The list of all available products.
 * @param inventories The list of products currently in inventory.
 * @param selectedProductId The ID of the currently selected product.
 * @param onProductSelected A callback function that is invoked when a product is selected.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun ProductDoubleSelectorField(
    products: List<ProductResponse>,
    inventories: List<InventoryResponse>,
    selectedProductId: String?,
    onProductSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Select Product",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Inventory Products Column
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    "Existing Inventory",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                if (inventories.isEmpty()) {
                    Text(
                        text = "No products in inventory",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                } else {
                    inventories.forEach { inventoryProduct ->
                        InventorySelectorCard (
                            inventory = inventoryProduct,
                            isSelected = selectedProductId == inventoryProduct.productId,
                            onClick = { onProductSelected(inventoryProduct.productId) }
                        )
                    }
                }
            }

            // All Products Column
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    "All Products",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                if (products.isEmpty()) {
                    Text(
                        text = "No products available",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                } else {
                    products.forEach { product ->
                        ProductSelectorCard(
                            product = product,
                            isSelected = selectedProductId == product.id,
                            onClick = { onProductSelected(product.id) }
                        )
                    }
                }
            }
        }
    }
}