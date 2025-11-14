package com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventorytransfer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventorysubtrack.components.InventorySelectorField
import com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventorytransfer.components.WarehouseSelectorField
import com.liquotrack.stocksip.shared.presentation.components.CustomTextField
import com.liquotrack.stocksip.shared.ui.components.TopAppBar

@Composable
fun InventoryTransferView(
    viewModel: InventoryTransferViewModel = hiltViewModel(),
    warehouseId: String? = null,
    onNavigateBack: () -> Unit
) {

    // Navigate back if warehouseId is null or empty
    // Also load inventory and warehouse list when warehouseId is valid
    LaunchedEffect(warehouseId) {
        if (warehouseId.isNullOrEmpty()) {
            onNavigateBack()
        } else {
            viewModel.loadInventoryList(warehouseId)
            viewModel.loadWarehouseList()
        }
    }

    val inventoryList by viewModel.inventoryList.collectAsState()
    val warehouseList by viewModel.warehouseList.collectAsState()

    val selectedProductId by viewModel.selectedProductId.collectAsState()
    val selectedWarehouseId by viewModel.selectedWarehouseId.collectAsState()
    val quantityToTransfer by viewModel.quantityToTransfer.collectAsState()
    val currentQuantity by viewModel.currentQuantity.collectAsState()
    val expirationDate by viewModel.expirationDate.collectAsState()

    val quantityError by viewModel.quantityError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    val isValidFormat =
        quantityToTransfer > 0 &&
                quantityToTransfer <= currentQuantity &&
                selectedProductId != null &&
                selectedWarehouseId != null &&
                quantityError.isNullOrEmpty()

    // Show 'quantity to transfer' error snack bar
    LaunchedEffect(quantityError) {
        quantityError?.let { error ->
            snackBarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.clearQuantityError()
        }
    }

    // Main Scaffold
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Transfer Products",
                onBackClick = onNavigateBack,
            )
        },
        containerColor = Color(0xFFF4ECEC),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color(0xFFB00020),
                    contentColor = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .background(Color(0xFFF4ECEC))
            ) {
                // Inventory Selector Card
                InventorySelectorField(
                    inventories = inventoryList,
                    selectedProductId = selectedProductId,
                    selectedExpirationDate = expirationDate,
                    onInventorySelected = { productId, expirationDate ->
                        viewModel.updateSelectedProductIdAndExpirationDate(productId, expirationDate)
                    }
                )

                // Space between sections
                Spacer(modifier = Modifier.height(24.dp))

                // Warehouse Selector Card
                WarehouseSelectorField(
                    warehouses = warehouseList,
                    selectedWarehouseId = selectedWarehouseId,
                    onWarehouseSelected = { warehouseId ->
                        viewModel.updateSelectedWarehouseId(warehouseId)
                    }
                )

                // Space between sections
                Spacer(modifier = Modifier.height(24.dp))

                // Input Fields Section
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Quantity to Transfer Input Field
                    CustomTextField(
                        value = if (quantityToTransfer == 0) "" else quantityToTransfer.toString(),
                        onValueChange = { newValue ->
                            val intValue = newValue.toIntOrNull() ?: 0
                            viewModel.validateAndUpdateQuantityToTransfer(intValue)
                        },
                        label = "Quantity to Transfer",
                        placeholder = "Enter quantity",
                        keyboardType = KeyboardType.Number,
                        isRequired = true,
                        showError = quantityError != null,
                    )
                }

                // Space at the bottom
                Spacer(modifier = Modifier.height(32.dp))

                // Inventory Summary Card
                // Shows final details before submission

                // Confirm Action Button
                Button(
                    onClick = {
                        viewModel.transferProduct(
                            originWarehouseId = warehouseId?:"",
                            onSuccess = onNavigateBack
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2B000D)
                    ),
                    enabled = isValidFormat && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Transfer",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}