package com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.productcreateoredit

import android.net.Uri
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductResponse
import com.liquotrack.stocksip.shared.presentation.components.CustomDoubleTextField
import com.liquotrack.stocksip.shared.presentation.components.CustomSpinnerField
import com.liquotrack.stocksip.shared.presentation.components.CustomTextField
import com.liquotrack.stocksip.shared.presentation.components.ImageSelectionSection
import com.liquotrack.stocksip.shared.ui.components.TopAppBar
import java.io.File

@Composable
fun StorageCreateOrEditView(
    viewModel: StorageCreateOrEditViewModel = hiltViewModel(),
    productId: String?,
    product: ProductResponse? = null,
    onNavigateBack : () -> Unit
) {

    val name by viewModel.productName.collectAsState()
    val type by viewModel.productType.collectAsState()
    val brand by viewModel.brand.collectAsState()
    val unitPrice by viewModel.unitPrice.collectAsState()
    val currencyCode by viewModel.currencyCode.collectAsState()
    val minimumStock by viewModel.minimumStock.collectAsState()
    var selectedImageFile by remember { mutableStateOf<File?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val isEditMode = productId != null && productId != "new" && productId.isNotBlank()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    val minimumStockError by viewModel.minimumStockError.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    val isValidFormat = name.isNotBlank() &&
            type.isNotBlank() &&
            brand.isNotBlank() &&
            unitPrice >= 0.0 &&
            currencyCode.isNotBlank() &&
            minimumStock >= 0

    // Load product details if in edit mode
    LaunchedEffect(productId) {
        if (isEditMode && product != null) {
            viewModel.loadProductForEdit(product)
        }
    }

    // Update UI when selected product changes
    LaunchedEffect(selectedProduct) {
        selectedProduct?.let { product ->
            viewModel.loadProductForEdit(product)
            if (product.imageUrl.isNotBlank()) {
                selectedImageUri = product.imageUrl.toUri()
            }
        }
    }

    // Show minimum stock error snack bar
    LaunchedEffect(minimumStockError) {
        minimumStockError?.let { error ->
            snackBarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.clearMinimumStockError()
        }
    }

    // Main Scaffold
    Scaffold(
        topBar = {
            TopAppBar(
                title = if (isEditMode) "Edit Product" else "New Product",
                onBackClick = onNavigateBack,
                isEditMode = isEditMode
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
                // Image Selection Section
                ImageSelectionSection(
                    selectedImage = selectedImageUri,
                    onImageSelected = { file, uri ->
                        selectedImageFile = file
                        selectedImageUri = uri
                    }
                )

                // Space between sections
                Spacer(modifier = Modifier.height(24.dp))

                // Input Fields Section
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Product Name
                    CustomTextField(
                        value = name,
                        onValueChange = viewModel::updateProductName,
                        label = "Name",
                        placeholder = "e.g., Blue Label"
                    )

                    // Row for Product Type and Brand
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Product Type
                        CustomSpinnerField(
                            items = emptyList(),
                            onItemSelected = viewModel::updateProductType,
                            label = "Type",
                            modifier = Modifier.weight(1f)
                        )

                        // Brand
                        CustomSpinnerField(
                            items = emptyList(),
                            onItemSelected = viewModel::updateBrand,
                            label = "Brand",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Row for Unit Price and Currency Code
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Unit Price
                        CustomDoubleTextField(
                            value = unitPrice,
                            onValueChange = viewModel::updateUnitPrice,
                            label = "Unit Price",
                            placeholder = "e.g., 199.99",
                            modifier = Modifier.weight(1f)
                        )

                       // Currency Code
                        CustomSpinnerField(
                            items = emptyList(),
                            onItemSelected = viewModel::updateCurrencyCode,
                            label = "Currency",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Minimum Stock
                    CustomTextField(
                        value = if (minimumStock == 0) "" else minimumStock.toString(),
                        onValueChange = { viewModel.updateMinimumStock(it.toIntOrNull() ?: 0) },
                        label = "Minimum Stock",
                        placeholder = "e.g., 10",
                        keyboardType = KeyboardType.Number,
                        showError = minimumStockError != null
                    )

                    // Space at the bottom
                    Spacer(modifier = Modifier.height(32.dp))

                    // Save Button
                    Button(
                        onClick = {
                            viewModel.updateImageFile(selectedImageFile)
                            viewModel.saveProduct(
                                isEditing = isEditMode,
                                productId = productId,
                                onSuccess = { onNavigateBack() }
                            )

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
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
                                text = if (isEditMode) "Update Product" else "Add Product",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}