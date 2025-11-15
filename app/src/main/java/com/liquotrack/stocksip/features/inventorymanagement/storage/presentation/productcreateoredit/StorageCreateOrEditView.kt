package com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.productcreateoredit

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.shared.presentation.components.*
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.shared.ui.components.TopBarWithBack
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageCreateOrEditView(
    viewModel: StorageCreateOrEditViewModel = hiltViewModel(),
    productId: String?,
    onNavigateBack: () -> Unit
) {

    val isEditMode = productId != null && productId != "new" && productId.isNotBlank()

    val brands by viewModel.brands.collectAsState()
    val types by viewModel.productTypes.collectAsState()
    val name by viewModel.productName.collectAsState()
    val type by viewModel.productType.collectAsState()
    val brand by viewModel.brand.collectAsState()
    val unitPrice by viewModel.unitPrice.collectAsState()
    val currencyCode by viewModel.currencyCode.collectAsState()
    val minimumStock by viewModel.minimumStock.collectAsState()
    val content by viewModel.content.collectAsState()
    var selectedImageFile by remember { mutableStateOf<File?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val isLoading by viewModel.isLoading.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    val minimumStockError by viewModel.minimumStockError.collectAsState()
    val contentError by viewModel.contentError.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val bg = Color(0xFFF4ECEC)

    LaunchedEffect(productId) {
        if (isEditMode && productId != "new") viewModel.getProductById(productId)
    }

    LaunchedEffect(selectedProduct) {
        selectedProduct?.let { viewModel.loadProductForEdit(it) }
    }

    val baseCurrencies = listOf("USD", "EUR", "INR", "GBP", "JPY")
    val currencies = remember(currencyCode) {
        if (currencyCode.isNotBlank() && !baseCurrencies.contains(currencyCode)) {
            listOf(currencyCode) + baseCurrencies
        } else baseCurrencies
    }
    var selectedCurrency by remember(currencyCode) { mutableStateOf(currencyCode.ifEmpty { "USD" }) }

    val isValidFormat = name.isNotBlank() &&
            type.isNotBlank() &&
            brand.isNotBlank() &&
            content >= 0.0 &&
            unitPrice >= 0.0 &&
            selectedCurrency.isNotBlank() &&
            minimumStock >= 0

    LaunchedEffect(minimumStockError) {
        minimumStockError?.let {
            snackBarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            viewModel.clearMinimumStockError()
        }
    }

    LaunchedEffect(contentError) {
        contentError?.let {
            snackBarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            viewModel.clearContentError()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                currentRoute = "storage",
                onNavigate = {},
                onClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBarWithBack(
                    title = if (isEditMode) stringResource(R.string.edit_product) else stringResource(R.string.new_product),
                    onBackClick = onNavigateBack
                )
            },
            containerColor = bg,
            snackbarHost = { SnackbarHost(snackBarHostState) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Image Selection Section
                ImageSelectionSection(
                    selectedImage = selectedImageUri,
                    onImageSelected = { file, uri ->
                        selectedImageFile = file
                        selectedImageUri = uri
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Product Name
                CustomTextField(
                    value = name,
                    onValueChange = viewModel::updateProductName,
                    label = stringResource(R.string.name),
                    placeholder = stringResource(R.string.name_product_placeholder)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Row for Product Type and Brand
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    CustomSpinnerField(
                        items = types,
                        onItemSelected = viewModel::updateProductType,
                        label = stringResource(R.string.type_product),
                        modifier = Modifier.weight(1f)
                    )

                    CustomSpinnerField(
                        items = brands,
                        onItemSelected = viewModel::updateBrand,
                        label = stringResource(R.string.brand),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Unit Price and Currency
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.unit_price),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = stringResource(R.string.currency),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                        var unitPriceText by remember { mutableStateOf(if (unitPrice == 0.0) "" else unitPrice.toString()) }
                        OutlinedTextField(
                            value = unitPriceText,
                            onValueChange = { value ->
                                unitPriceText = value
                                viewModel.updateUnitPrice(value.toDoubleOrNull() ?: 0.0)
                            },
                            placeholder = { Text("18") },
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = Color(0xFF2B000D),
                                unfocusedBorderColor = Color.LightGray,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            )
                        )

                        var expanded by remember { mutableStateOf(false) }
                        Column(modifier = Modifier.weight(1f)) {
                            TextField(
                                value = selectedCurrency,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(stringResource(R.string.currency_required)) },
                                trailingIcon = {
                                    IconButton(onClick = { expanded = !expanded }) {
                                        Icon(
                                            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                            contentDescription = null
                                        )
                                    }
                                },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                currencies.forEach { currency ->
                                    DropdownMenuItem(
                                        text = { Text(currency) },
                                        onClick = {
                                            selectedCurrency = currency
                                            viewModel.updateCurrencyCode(currency)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Minimum Stock and Content
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    CustomTextField(
                        value = if (minimumStock == 0) "" else minimumStock.toString(),
                        onValueChange = { viewModel.updateMinimumStock(it.toIntOrNull() ?: 0) },
                        label = stringResource(R.string.minimum_stock),
                        placeholder = stringResource(R.string.minimum_stock_placeholder),
                        keyboardType = KeyboardType.Number,
                        showError = minimumStockError != null,
                        isRequired = true,
                        modifier = Modifier.weight(1f)
                    )

                    CustomDoubleTextField(
                        value = content,
                        onValueChange = { viewModel.updateProductContent(it) },
                        label = stringResource(R.string.product_content),
                        placeholder = stringResource(R.string.product_content_placeholder),
                        modifier = Modifier.weight(1f),
                        showError = contentError != null,
                        isRequired = true
                    )
                }

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
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B000D)),
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
                            text = if (isEditMode) stringResource(R.string.update_product) else stringResource(R.string.add_product),
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
