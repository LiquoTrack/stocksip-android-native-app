package com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.productdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.productdetail.components.InfoRow
import com.liquotrack.stocksip.shared.ui.components.TopAppBar

/**
 * Composable function for displaying product details.
 * Also handles navigation and user interactions for editing and deleting the selected product.
 *
 * @param viewModel The ViewModel managing product detail state and actions which is injected by Hilt
 * @param productId The identifier of the product to display
 * @param onNavigateBack Callback function to navigate back to the previous screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailView(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    productId: String,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        if (productId.isNotBlank()) viewModel.getProductById(productId)
    }

    val selectedProduct by viewModel.selectedProduct.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var quantity by remember { mutableIntStateOf(1) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    if (selectedProduct == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF8B4C5C))
        }
        return
    }

    val product = selectedProduct!!

    Box(modifier = Modifier.fillMaxSize()) {
        // Background gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF2D1B2E), Color(0xFF5C1F2E))
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Product Image Card
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
                        if (product.imageUrl.isNotEmpty()) {
                            AsyncImage(
                                model = product.imageUrl,
                                contentDescription = product.name,
                                modifier = Modifier.fillMaxSize().padding(24.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    // Stock label
                    val stockColor = if (product.totalStockInWarehouse > 0) Color(0xFF4CAF50) else Color(0xFFFF5252)
                    val stockText = if (product.totalStockInWarehouse > 0)
                        stringResource(R.string.in_stock)
                    else stringResource(R.string.out_of_stock)
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = stockColor
                    ) {
                        Text(
                            stockText,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Detail Card
                Card(
                    modifier = Modifier.fillMaxWidth().offset(y = (-30).dp),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F4F4))
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                        Text(
                            product.name,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D1B2E)
                        )
                        Spacer(Modifier.height(16.dp))

                        // Price
                        val priceText = "${product.unitPrice} ${product.currencyCode}"
                        Text(
                            priceText,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF8B4C5C)
                        )
                        Spacer(Modifier.height(24.dp))

                        // Info rows
                        InfoRow(label = stringResource(R.string.minimum_stock) + ": ", value = product.minimumStock.toString())
                        InfoRow(label = stringResource(R.string.total_stored) + ": ", value = product.totalStockInWarehouse.toString())
                        InfoRow(label = stringResource(R.string.content_ml) + ": ", value = product.content.toString())
                        Spacer(Modifier.height(24.dp))

                        Text(stringResource(R.string.care_guide_associated), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF2D1B2E))
                        Spacer(Modifier.height(100.dp))
                    }
                }
            }
        }

        // Floating Action Buttons
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FloatingActionButton(
                onClick = {
                    if (product.totalStockInWarehouse == 0) showDeleteConfirm = true
                    else showErrorDialog = true
                },
                containerColor = MaterialTheme.colorScheme.error
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Product")
            }

            FloatingActionButton(
                onClick = { onNavigateToEdit(product.id) },
                containerColor = Color(0xFF8B4C5C)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Product")
            }
        }
    }

    // Confirm deletion
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text(stringResource(R.string.confirm_deletion)) },
            text = { Text(stringResource(R.string.confirm_deletion_text)) },
            confirmButton = { TextButton(onClick = { /* ... */ }) { Text(stringResource(R.string.delete)) } },
            dismissButton = { TextButton(onClick = { showDeleteConfirm = false }) { Text(stringResource(R.string.cancel)) } }
        )
    }

    // Error dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(stringResource(R.string.cannot_delete)) },
            text = { Text(stringResource(R.string.product_has_stock)) },
            confirmButton = { TextButton(onClick = { showErrorDialog = false }) { Text(stringResource(R.string.ok)) } }
        )
    }
}
