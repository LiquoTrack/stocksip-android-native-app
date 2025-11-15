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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventorysubtrack.components.InventorySelectorField
import com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventorytransfer.components.WarehouseSelectorField
import com.liquotrack.stocksip.shared.presentation.components.CustomTextField
import com.liquotrack.stocksip.shared.ui.components.TopAppBar
import com.liquotrack.stocksip.shared.ui.components.TopBarWithBack

@Composable
fun InventoryTransferView(
    viewModel: InventoryTransferViewModel = hiltViewModel(),
    warehouseId: String? = null,
    onNavigateBack: () -> Unit
) {

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

    LaunchedEffect(warehouseId) {
        if (warehouseId.isNullOrEmpty()) {
            onNavigateBack()
        } else {
            viewModel.loadInventoryList(warehouseId)
            viewModel.loadWarehouseList()
        }
    }

    LaunchedEffect(quantityError) {
        quantityError?.let { error ->
            snackBarHostState.showSnackbar(message = error, duration = SnackbarDuration.Short)
            viewModel.clearQuantityError()
        }
    }

    Scaffold(
        topBar = {
            TopBarWithBack(
                title = stringResource(R.string.transfer_products),
                onBackClick = onNavigateBack
            )
        },
        containerColor = Color(0xFFF4ECEC),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .background(Color(0xFFF4ECEC))
            ) {
                InventorySelectorField(
                    inventories = inventoryList,
                    selectedProductId = selectedProductId,
                    selectedExpirationDate = expirationDate,
                    onInventorySelected = { productId, expirationDate, currentQuantity ->
                        viewModel.updateOnSelectedInventory(productId, expirationDate, currentQuantity)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                WarehouseSelectorField(
                    warehouses = warehouseList,
                    selectedWarehouseId = selectedWarehouseId,
                    onWarehouseSelected = { viewModel.updateSelectedWarehouseId(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                CustomTextField(
                    value = if (quantityToTransfer == 0) "" else quantityToTransfer.toString(),
                    onValueChange = { newValue ->
                        viewModel.validateAndUpdateQuantityToTransfer(newValue.toIntOrNull() ?: 0)
                    },
                    label = stringResource(R.string.quantity_to_transfer),
                    placeholder = stringResource(R.string.enter_quantity),
                    keyboardType = KeyboardType.Number,
                    isRequired = true,
                    showError = quantityError != null
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { viewModel.transferProduct(warehouseId ?: "", onNavigateBack) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B000D)),
                    enabled = isValidFormat && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text(text = stringResource(R.string.transfer), fontSize = 16.sp, color = Color.White)
                    }
                }
            }
        }
    }
}
