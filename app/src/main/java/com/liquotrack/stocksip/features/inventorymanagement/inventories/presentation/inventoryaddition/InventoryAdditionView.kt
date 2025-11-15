package com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventoryaddition

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventoryaddition.components.ProductDoubleSelectorField
import com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventoryaddition.components.ProductSelectorField
import com.liquotrack.stocksip.shared.presentation.components.CustomTextField
import com.liquotrack.stocksip.shared.presentation.components.DateInputField
import com.liquotrack.stocksip.shared.ui.components.TopAppBar
import com.liquotrack.stocksip.shared.ui.components.TopBarWithBack
import com.liquotrack.stocksip.shared.utils.stringToDate

/**
 * Composable function for the Inventory Addition View.
 *
 * @param viewModel The ViewModel for managing inventory addition state and logic.
 * @param warehouseId The ID of the warehouse to which products are being added.
 * @param onNavigateBack Callback function to navigate back to the previous screen.
 */
@Composable
fun InventoryAdditionView(
    viewModel: InventoryAdditionViewModel = hiltViewModel(),
    warehouseId: String? = null,
    onNavigateBack: () -> Unit,
) {

    LaunchedEffect(warehouseId) {
        if (warehouseId.isNullOrEmpty()) {
            onNavigateBack()
        } else {
            viewModel.loadProductList(warehouseId)
        }
    }

    val selectedProductId by viewModel.selectedProductId.collectAsState()
    val productsList by viewModel.productList.collectAsState()
    val inventories by viewModel.inventoryList.collectAsState()
    val quantityToAdd by viewModel.quantityToAdd.collectAsState()
    val expirationDate by viewModel.expirationDate.collectAsState()
    val quantityError by viewModel.quantityError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    val isValidFormat = quantityToAdd > 0 &&
            selectedProductId != null
            && quantityError.isNullOrEmpty()

    LaunchedEffect(quantityError) {
        quantityError?.let { error ->
            snackBarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.clearQuantityError()
        }
    }

    Scaffold(
        topBar = {
            TopBarWithBack(
                title = stringResource(R.string.add_products),
                onBackClick = onNavigateBack
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
                ProductDoubleSelectorField(
                    products = productsList,
                    inventories = inventories,
                    selectedProductId = selectedProductId,
                    onProductSelected = { viewModel.updateSelectedProductId(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    CustomTextField(
                        value = if (quantityToAdd == 0) "" else quantityToAdd.toString(),
                        onValueChange = { newValue ->
                            val intValue = newValue.toIntOrNull() ?: 0
                            viewModel.updateQuantityToAdd(intValue)
                        },
                        label = stringResource(R.string.quantity_to_add),
                        placeholder = stringResource(R.string.enter_quantity),
                        keyboardType = KeyboardType.Number,
                        isRequired = true,
                        showError = quantityError != null,
                    )

                    DateInputField(
                        label = stringResource(R.string.expiration_date_optional),
                        isRequired = false,
                        onDateChange = { dateString ->
                            viewModel.updateExpirationDate(stringToDate(dateString))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        viewModel.saveInventoryAddition(
                            warehouseId = warehouseId ?: "",
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
                            text = stringResource(R.string.add_products),
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
