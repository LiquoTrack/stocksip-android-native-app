package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.liquotrack.stocksip.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse.WarehouseViewModel
import com.liquotrack.stocksip.shared.ui.components.TopBarWithBack
import kotlinx.coroutines.launch

@Composable
fun CatalogCreateAndEditScreen(
    isEditMode: Boolean = false,
    catalogId: String? = null,
    onBack: () -> Unit,
    catalogViewModel: CatalogViewModel = hiltViewModel(),
    warehouseViewModel: WarehouseViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val catalogs by catalogViewModel.catalogs.collectAsStateWithLifecycle()
    val selectedCatalog = catalogs.find { it.id == catalogId }
    val pendingItems by catalogViewModel.pendingItems.collectAsStateWithLifecycle()

    val warehouses by warehouseViewModel.warehouses.collectAsStateWithLifecycle()
    val warehouseProducts by warehouseViewModel.products.collectAsStateWithLifecycle()

    var catalogName by remember { mutableStateOf(selectedCatalog?.name ?: "") }
    var catalogDescription by remember { mutableStateOf(selectedCatalog?.description ?: "") }
    var contactEmail by remember { mutableStateOf(selectedCatalog?.contactEmail ?: "") }
    var isPublished by remember { mutableStateOf(selectedCatalog?.isPublished ?: false) }

    var selectedWarehouseId by remember { mutableStateOf<String?>(null) }
    var showWarehouseDialog by remember { mutableStateOf(false) }
    var showProductDialog by remember { mutableStateOf(false) }
    val selectedProductIds = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        catalogViewModel.loadCatalogsByAccount()
        warehouseViewModel.getAllWarehousesByAccountId()
    }

    LaunchedEffect(selectedWarehouseId) {
        selectedWarehouseId?.let {
            warehouseViewModel.loadProductsByWarehouse(it)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (!isEditMode) catalogViewModel.clearPendingItems()
        }
    }

    // Warehouse dialog
    if (showWarehouseDialog) {
        AlertDialog(
            onDismissRequest = { showWarehouseDialog = false },
            title = { Text(stringResource(R.string.select_warehouse)) },
            text = {
                LazyColumn {
                    items(warehouses?.warehouses ?: emptyList()) { warehouse ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    selectedWarehouseId = warehouse.id
                                    selectedProductIds.clear()
                                    showWarehouseDialog = false
                                    showProductDialog = true
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedWarehouseId == warehouse.id)
                                    Color(0xFFF7E7E8) else Color.White
                            )
                        ) {
                            Text(
                                text = warehouse.name,
                                modifier = Modifier.padding(16.dp),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showWarehouseDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    // Product dialog
    if (showProductDialog && selectedWarehouseId != null) {
        val productStockInputs = remember { mutableStateMapOf<String, String>() }

        AlertDialog(
            onDismissRequest = {
                showProductDialog = false
                selectedProductIds.clear()
                productStockInputs.clear()
            },
            title = { Text(stringResource(R.string.select_products_to_add)) },
            text = {
                LazyColumn {
                    items(warehouseProducts) { product ->
                        val isSelected = selectedProductIds.contains(product.id)
                        val isAlreadyInCatalog = if (isEditMode && selectedCatalog != null) {
                            selectedCatalog.catalogItems.any { it.productId == product.id }
                        } else {
                            pendingItems.any { it.productId == product.id }
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable(enabled = !isAlreadyInCatalog) {
                                    if (isSelected) selectedProductIds.remove(product.id)
                                    else selectedProductIds.add(product.id)
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = when {
                                    isAlreadyInCatalog -> Color(0xFFE0E0E0)
                                    isSelected -> Color(0xFFF7E7E8)
                                    else -> Color.White
                                }
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (product.imageUrl != null) {
                                        AsyncImage(
                                            model = product.imageUrl,
                                            contentDescription = product.name,
                                            modifier = Modifier
                                                .size(50.dp)
                                                .background(Color(0xFFF7E7E8), RoundedCornerShape(8.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .size(50.dp)
                                                .background(Color(0xFFF7E7E8), RoundedCornerShape(8.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("?", fontSize = 20.sp, color = Color.Gray)
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(product.name, fontWeight = FontWeight.Medium)
                                        Text("${product.price} ${product.currency}", color = Color.Gray, fontSize = 12.sp)
                                    }

                                    if (isAlreadyInCatalog) {
                                        Text(
                                            stringResource(R.string.added),
                                            color = Color.Gray,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    } else {
                                        Checkbox(
                                            checked = isSelected,
                                            onCheckedChange = {
                                                if (isSelected) selectedProductIds.remove(product.id)
                                                else selectedProductIds.add(product.id)
                                            }
                                        )
                                    }
                                }

                                if (isSelected && !isAlreadyInCatalog) {
                                    Spacer(Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = productStockInputs[product.id] ?: "",
                                        onValueChange = { productStockInputs[product.id] = it },
                                        label = { Text(stringResource(R.string.enter_stock)) },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth(),
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                                        ),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFF8B4C5C),
                                            unfocusedBorderColor = Color.LightGray
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showProductDialog = false
                    if (isEditMode && catalogId != null && selectedWarehouseId != null) {
                        selectedProductIds.forEach { productId ->
                            val enteredStock = productStockInputs[productId]?.toIntOrNull() ?: 0
                            if (enteredStock > 0) {
                                scope.launch {
                                    catalogViewModel.addCatalogItem(
                                        catalogId, productId, selectedWarehouseId!!, enteredStock
                                    )
                                }
                            } else Log.w("CATALOG", "⚠️ Product $productId without valid stock")
                        }
                    } else if (selectedWarehouseId != null) {
                        selectedProductIds.forEach { productId ->
                            val enteredStock = productStockInputs[productId]?.toIntOrNull() ?: 0
                            if (enteredStock > 0) {
                                catalogViewModel.addPendingItem(productId, selectedWarehouseId!!, enteredStock)
                            }
                        }
                    }
                    selectedProductIds.clear()
                    productStockInputs.clear()
                }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showProductDialog = false
                    selectedProductIds.clear()
                    productStockInputs.clear()
                }) {
                    Text(stringResource(R.string.cancel_catalog))
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4ECEC))
    ) {
        TopBarWithBack(
            title = if (isEditMode)
                stringResource(R.string.edit_catalog)
            else stringResource(R.string.new_catalog),
            onBackClick = onBack,
            actions = {
                if (isEditMode && catalogId != null) {
                    IconButton(onClick = {
                        Log.d("CATALOG", "Delete catalog $catalogId")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete),
                            tint = Color(0xFFE8B4BE)
                        )
                    }
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.catalog_info), color = Color(0xFFE8B4BE), fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = catalogName,
                onValueChange = { catalogName = it },
                placeholder = { Text(stringResource(R.string.name_placeholder_catalog), color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = catalogDescription,
                onValueChange = { catalogDescription = it },
                placeholder = { Text(stringResource(R.string.description_placeholder), color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = contactEmail,
                onValueChange = { contactEmail = it },
                placeholder = { Text(stringResource(R.string.contact_email_placeholder), color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.catalog_items), color = Color(0xFFE8B4BE), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                IconButton(onClick = { showWarehouseDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_products),
                        tint = Color(0xFF8B4C5C)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    if (isEditMode) {
                        val items = selectedCatalog?.catalogItems ?: emptyList()

                        if (items.isEmpty()) {
                            item {
                                Text(
                                    "No products in this catalog",
                                    color = Color.Gray,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }
                        } else {
                            items(items) { item ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.productName,
                                            color = Color.Black,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = item.unitPrice ?: "N/A",
                                            color = Color.Gray,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = "Stock: ${item.availableStock}",
                                            color = Color.Gray,
                                            fontSize = 12.sp
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            scope.launch {
                                                catalogViewModel.removeCatalogItem(catalogId!!, item.productId)
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Remove",
                                            tint = Color.Red
                                        )
                                    }
                                }
                                Spacer(Modifier.height(8.dp))
                                Divider()
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    } else {
                        if (pendingItems.isEmpty()) {
                            item {
                                Text(
                                    "No products selected yet. Click + to add products.",
                                    color = Color.Gray,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }
                        } else {
                            items(pendingItems) { pendingItem ->
                                val product = warehouseProducts.find { it.id == pendingItem.productId }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = product?.name ?: "Product",
                                            color = Color.Black,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = product?.let { "${it.price} ${it.currency}" } ?: "N/A",
                                            color = Color.Gray,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = "Stock: ${pendingItem.stock}",
                                            color = Color.Gray,
                                            fontSize = 12.sp
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            catalogViewModel.removePendingItem(pendingItem.productId)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Remove",
                                            tint = Color.Red
                                        )
                                    }
                                }
                                Spacer(Modifier.height(8.dp))
                                Divider()
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            if (isEditMode) {
                Text("Status", color = Color(0xFFE8B4BE), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Publish/Unpublish", color = Color.Black, fontSize = 16.sp)
                    Switch(
                        checked = isPublished,
                        onCheckedChange = { isPublished = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFFE8B4BE),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.Gray
                        )
                    )
                }

                Spacer(Modifier.height(16.dp))
            }

            Button(
                onClick = {
                    scope.launch {
                        if (isEditMode && catalogId != null) {
                            catalogViewModel.updateCatalog(catalogId, catalogName, catalogDescription, contactEmail)
                            if (isPublished) catalogViewModel.publishCatalog(catalogId)
                            else catalogViewModel.unpublishCatalog(catalogId)
                        } else {
                            catalogViewModel.createCatalog(catalogName, catalogDescription, contactEmail)
                        }
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C1F2E)),
                shape = RoundedCornerShape(25.dp),
                enabled = catalogName.isNotBlank()
            ) {
                Text(
                    text = if (isEditMode)
                        stringResource(R.string.save)
                    else
                        stringResource(R.string.create),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}