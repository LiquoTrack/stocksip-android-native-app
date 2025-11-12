package com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.productdetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

/**
 * Composable function for displaying product details.
 * Also handles navigation and user interactions for editing and deleting the selected product.
 *
 * @param viewModel The ViewModel managing product detail state and actions which is injected by Hilt
 * @param productId The identifier of the product to display
 * @param onNavigateBack Callback function to navigate back to the previous screen
 */
@Composable
fun ProductDetailView(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    productId: String,
    onNavigateBack : () -> Unit
) {
    val selectedProduct = viewModel.selectedProduct.collectAsState()

    selectedProduct.value?.let { product ->
        Scaffold (
            floatingActionButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Delete Button
                    FloatingActionButton(
                        onClick = {
                            // Handle delete action
                        },
                        containerColor = MaterialTheme.colorScheme.inversePrimary,
                        contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Delete Product",
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .padding(8.dp)
                        )
                    }

                    // Edit Button
                    FloatingActionButton(
                        onClick = {
                            // Handle edit action
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Product",
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .padding(8.dp)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {

            }
        }
    }
}