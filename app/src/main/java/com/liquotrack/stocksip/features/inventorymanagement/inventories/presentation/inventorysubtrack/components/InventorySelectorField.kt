package com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventorysubtrack.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryResponse
import java.util.Date
import kotlin.collections.forEach

/**
 * A composable function that displays an inventory selector field with a dropdown menu.
 *
 * @param inventories The list of available inventories in the warehouse to select from.
 * @param selectedProductId The ID of the currently selected inventory product.
 * @param selectedExpirationDate The expiration date of the currently selected inventory product.
 * @param onInventorySelected A callback function that is invoked when a product is selected.
 */
@Composable
fun InventorySelectorField(
    inventories: List<InventoryResponse>,
    selectedProductId: String?,
    selectedExpirationDate: Date?,
    onInventorySelected: (String, Date) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedProduct = inventories.find { it.id == selectedProductId }
    val selectedExpirationDate = inventories.find { it.expirationDate == selectedExpirationDate }

    Box {
        // Button to open the dropdown
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                AsyncImage(
                    model = selectedProduct?.imageUrl,
                    contentDescription = "Selected Product Image",
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = selectedProduct?.name ?: "Select a product",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // Dropdown with all products options
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            inventories.forEach { inventory ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = inventory.imageUrl,
                                contentDescription = inventory.name,
                                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(6.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(inventory.name)
                        }
                    },
                    onClick = {
                        onInventorySelected(inventory.id, inventory.expirationDate ?: Date())
                        expanded = false
                    }
                )
            }
        }
    }
}