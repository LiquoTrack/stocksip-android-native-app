package com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventorytransfer.components

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
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseResponse
import kotlin.collections.forEach

/**
 * A composable function that displays a warehouse selector field with a dropdown menu.
 *
 * @param warehouses The list of available warehouses to select from.
 * @param selectedWarehouseId The ID of the currently selected warehouse.
 * @param onWarehouseSelected A callback function that is invoked when a warehouse is selected.
 */
@Composable
fun WarehouseSelectorField(
    warehouses: List<WarehouseResponse>,
    selectedWarehouseId: String?,
    onWarehouseSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedProduct = warehouses.find { it.id == selectedWarehouseId }

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
                    contentDescription = "Selected Warehouse Image",
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = selectedProduct?.name ?: "Select a warehouse",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // Dropdown with all the warehouses options
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            warehouses.forEach { warehouse ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = warehouse.imageUrl,
                                contentDescription = warehouse.name,
                                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(6.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(warehouse.name)
                        }
                    },
                    onClick = {
                        onWarehouseSelected(warehouse.id)
                        expanded = false
                    }
                )
            }
        }
    }
}