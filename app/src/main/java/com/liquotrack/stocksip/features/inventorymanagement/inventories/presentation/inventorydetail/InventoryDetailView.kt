package com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventorydetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.productdetail.components.InfoRow
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * Inventory Detail View Composable
 * Displays detailed information about a specific inventory item.
 *
 * @param viewModel The ViewModel for managing inventory detail state.
 * @param inventoryId The ID of the inventory item to display.
 * @param onNavigateBack Callback function to navigate back to the previous screen.
 */
@Composable
fun InventoryDetailView (
    viewModel: InventoryDetailViewModel = hiltViewModel(),
    inventoryId: String,
    onNavigateBack: () -> Unit
) {

    LaunchedEffect(Unit) {
        if (inventoryId != "") {
            viewModel.getInventoryById(inventoryId)
        }
    }

    val selectedInventory by viewModel.selectedInventory.collectAsState()

    if (selectedInventory == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val inventory = selectedInventory!!
        var showDeleteConfirm by remember { mutableStateOf(false) }
        var showErrorOnDeleteDialog by remember { mutableStateOf(false) }

        selectedInventory?.let {
            Scaffold(
                floatingActionButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Delete FAB
                        FloatingActionButton(
                            onClick = {
                                if (inventory.quantity == 0) {
                                    showDeleteConfirm = true
                                } else {
                                    showErrorOnDeleteDialog = true
                                }
                            },
                            containerColor = MaterialTheme.colorScheme.error
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Inventory of Product")
                        }
                    }
                },
                floatingActionButtonPosition = FabPosition.Center
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .padding(bottom = 88.dp)
                    ) {
                        // Card with product basic info
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Product Image
                                AsyncImage(
                                    model = inventory.imageUrl,
                                    contentDescription = "Product Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(width = 96.dp, height = 96.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )

                                // Spacer
                                Spacer(modifier = Modifier.padding(12.dp))

                                // Product Details
                                Column(modifier = Modifier.weight(1f)) {

                                    // Product Name
                                    Text(
                                        text = "Name: " + inventory.name,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Product Type
                                    Text(
                                        text = "Type: " + inventory.type,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Product Brand
                                    Text(
                                        text = "Brand: " + inventory.brand,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // More Details Section
                        Surface(
                            tonalElevation = 2.dp,
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.background,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "More Details",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                // Visible divider
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    thickness = DividerDefaults.Thickness,
                                    color = DividerDefaults.color
                                )

                                // Current State
                                InfoRow(
                                    label = "Current State: ",
                                    value = inventory.currentState
                                )

                                // Unit Price + Currency Formatting
                                val priceText = run {
                                    try {
                                        val nf =
                                            NumberFormat.getCurrencyInstance(Locale.getDefault())
                                        try {
                                            nf.currency =
                                                Currency.getInstance(inventory.moneyCode)
                                        } catch (_: Exception) {
                                        }
                                        nf.format(inventory.unitPrice)
                                    } catch (_: Exception) {
                                        "${inventory.unitPrice} ${inventory.moneyCode}"
                                    }
                                }

                                // Unit Price
                                InfoRow(label = "Unit Price: ", value = priceText)

                                // Minimum Stock Level
                                InfoRow(
                                    label = "Minimum Stock: ",
                                    value = inventory.minimumStock.toString()
                                )

                                // Quantity Stored
                                InfoRow(
                                    label = "Quantity Stored: ",
                                    value = inventory.quantity.toString()
                                )
                            }
                        }
                    }

                    // Confirm deletion dialog
                    if (showDeleteConfirm) {
                        AlertDialog(
                            onDismissRequest = { showDeleteConfirm = false },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        showDeleteConfirm = false
                                        viewModel.deleteInventoryById(
                                            inventoryId = inventoryId,
                                            onSuccess = { onNavigateBack() }
                                        )
                                    }
                                ) {
                                    Text(text = "Delete")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDeleteConfirm = false }) {
                                    Text(text = "Cancel")
                                }
                            },
                            title = { Text(text = "Confirm deletion") },
                            text = {
                                Text(text = "Are you sure you want to delete this product from this warehouse? This action cannot be undone.")
                            }
                        )
                    }

                    // Error on delete dialog
                    if (showErrorOnDeleteDialog) {
                        Dialog(
                            onDismissRequest = { showErrorOnDeleteDialog = false }
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                                    .padding(20.dp)
                            ) {
                                Text(
                                    text = "This product cannot be deleted from this warehouse because it still has stock available in it.",
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
