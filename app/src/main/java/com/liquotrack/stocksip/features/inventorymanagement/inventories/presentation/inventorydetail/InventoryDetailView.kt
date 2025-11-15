package com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventorydetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.liquotrack.stocksip.R
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryDetailView(
    viewModel: InventoryDetailViewModel = hiltViewModel(),
    inventoryId: String,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        if (inventoryId != "") viewModel.getInventoryById(inventoryId)
    }

    val selectedInventory by viewModel.selectedInventory.collectAsState()
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showErrorOnDeleteDialog by remember { mutableStateOf(false) }

    if (selectedInventory == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val inventory = selectedInventory!!

    Box(modifier = Modifier.fillMaxSize()) {
        // Background gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2D1B2E),
                            Color(0xFF5C1F2E)
                        )
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Product Image Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .size(240.dp)
                            .shadow(
                                elevation = 24.dp,
                                shape = RoundedCornerShape(24.dp),
                                spotColor = Color(0xFF8B4C5C)
                            ),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        AsyncImage(
                            model = inventory.imageUrl,
                            contentDescription = inventory.name,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize().padding(24.dp)
                        )
                    }
                }

                // Content Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-30).dp),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F4F4))
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                        Text(
                            inventory.name,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D1B2E)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Type: ${inventory.type} | Brand: ${inventory.brand}",
                            fontSize = 16.sp,
                            color = Color(0xFF666666)
                        )

                        Spacer(Modifier.height(24.dp))

                        Surface(
                            tonalElevation = 2.dp,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                val priceText = runCatching {
                                    NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
                                        currency = Currency.getInstance(inventory.moneyCode)
                                    }.format(inventory.unitPrice)
                                }.getOrElse { "${inventory.unitPrice} ${inventory.moneyCode}" }

                                InfoRow(label = stringResource(R.string.current_state), value = inventory.currentState)
                                InfoRow(label = stringResource(R.string.unit_price), value = priceText)
                                InfoRow(label = stringResource(R.string.minimum_stock), value = inventory.minimumStock.toString())
                                InfoRow(label = stringResource(R.string.quantity_stored), value = inventory.quantity.toString())
                            }
                        }

                        Spacer(Modifier.height(100.dp))
                    }
                }
            }
        }

        // Floating Delete Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    if (inventory.quantity == 0) showDeleteConfirm = true
                    else showErrorOnDeleteDialog = true
                },
                containerColor = MaterialTheme.colorScheme.error,
            ) {
                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete_inventory))
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
                    ) { Text(stringResource(R.string.delete)) }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirm = false }) { Text(stringResource(R.string.cancel)) }
                },
                title = { Text(stringResource(R.string.confirm_deletion)) },
                text = { Text(stringResource(R.string.confirm_deletion_message)) }
            )
        }

        // Error dialog if still has stock
        if (showErrorOnDeleteDialog) {
            Dialog(onDismissRequest = { showErrorOnDeleteDialog = false }) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(20.dp)
                ) {
                    Text(
                        stringResource(R.string.cannot_delete_stock_available),
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
