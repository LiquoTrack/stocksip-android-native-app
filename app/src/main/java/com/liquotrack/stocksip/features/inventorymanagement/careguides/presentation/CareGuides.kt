package com.liquotrack.stocksip.features.inventorymanagement.careguides.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.core.navigation.Route
import com.liquotrack.stocksip.features.authentication.login.presentation.login.LoginViewModel
import com.liquotrack.stocksip.features.inventorymanagement.careguides.domain.CareGuide
import com.liquotrack.stocksip.features.inventorymanagement.careguides.domain.CareGuideViewModel
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductResponse
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.shared.ui.components.TopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareGuides(
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: CareGuideViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val search = remember { mutableStateOf("") }
    val careGuides = viewModel.careGuides.collectAsState()
    val products = viewModel.products.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedGuide by remember { mutableStateOf<CareGuide?>(null) }
    val showDetailDialog = selectedGuide != null
    val context = LocalContext.current
    var assignGuide by remember { mutableStateOf<CareGuide?>(null) }
    var selectedProductId by remember { mutableStateOf<String?>(null) }
    var isAssigning by remember { mutableStateOf(false) }
    var assignError by remember { mutableStateOf<String?>(null) }
    val isLoggedOut by loginViewModel.isLoggedOut.collectAsState()

    val selectedProduct = products.value.firstOrNull { it.id == selectedProductId }

    LaunchedEffect(products.value) {
        selectedProductId?.let { currentId ->
            if (products.value.none { it.id == currentId }) {
                selectedProductId = null
            }
        }
    }

    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            onLogout()
            loginViewModel.resetLogoutState()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                currentRoute = "care_guide",
                onNavigate = onNavigate,
                onClose = { scope.launch { drawerState.close() } },
                onLogout = { loginViewModel.logout() }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = stringResource(R.string.care_guides_title),
                    showBackButton = false,
                    onNavigationClick = { scope.launch { drawerState.open() } }
                )
            },
            containerColor = Color(0xFFF4ECEC)
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = search.value,
                        onValueChange = { search.value = it },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        },
                        placeholder = {
                            Text(stringResource(R.string.placeholder_search), color = Color.Gray)
                        },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = { onNavigate(Route.CareGuideCreate.route) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4A1B2A),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.new_button))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(careGuides.value.size) { index ->
                        val careGuide = careGuides.value[index]
                        CareGuideCard(
                            careGuide = careGuide,
                            onClick = {},
                            onSeeGuide = { selectedGuide = it },
                            onEdit = { onNavigate(Route.CareGuideEdit.buildRoute(it.careGuideId)) },
                            onAssign = {
                                viewModel.loadProducts()
                                assignGuide = it
                                selectedProductId = null
                                assignError = null
                            }
                        )
                    }
                }
            }
        }
    }

    if (showDetailDialog) {
        CareGuideDetailDialog(
            careGuide = selectedGuide!!,
            onDismiss = { selectedGuide = null }
        )
    }

    if (assignGuide != null) {
        AssignCareGuideDialog(
            careGuide = assignGuide!!,
            products = products.value,
            selectedProduct = selectedProduct,
            isLoading = isAssigning,
            errorMessage = assignError,
            onProductSelected = { selectedProductId = it.id },
            onDismiss = {
                assignGuide = null
                selectedProductId = null
                assignError = null
            },
            onConfirm = {
                if (products.value.isEmpty()) {
                    assignError = context.getString(R.string.error_no_products_available)
                    return@AssignCareGuideDialog
                }

                val chosenProduct = selectedProduct
                if (chosenProduct == null) {
                    assignError = context.getString(R.string.error_empty_product_id)
                    return@AssignCareGuideDialog
                }
                scope.launch {
                    isAssigning = true
                    val success = viewModel.assignCareGuide(assignGuide!!.careGuideId, chosenProduct.id)
                    isAssigning = false
                    if (success) {
                        assignGuide = null
                        selectedProductId = null
                        assignError = null
                    } else {
                        assignError = context.getString(R.string.error_assign_care_guide)
                    }
                }
            }
        )
    }
}

@Composable
private fun CareGuideDetailDialog(
    careGuide: CareGuide,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (careGuide.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(careGuide.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = careGuide.productName,
                        modifier = Modifier
                            .height(140.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .height(140.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFE9D9CA)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalDrink,
                            contentDescription = careGuide.productName,
                            tint = Color(0xFF8A3040)
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    DetailRow(title = stringResource(R.string.product_name_label), value = careGuide.productName)
                    DetailRow(
                        title = stringResource(R.string.type),
                        value = careGuide.title.ifBlank { stringResource(R.string.not_available) }
                    )
                    DetailRow(title = stringResource(R.string.comments), value = careGuide.summary)
                    DetailRow(
                        title = stringResource(R.string.min_temp),
                        value = "${careGuide.recommendedMinTemperature}° C"
                    )
                    DetailRow(
                        title = stringResource(R.string.max_temp),
                        value = "${careGuide.recommendedMaxTemperature}° C"
                    )
                }

                TextButton(onClick = onDismiss) {
                    Text(
                        text = stringResource(R.string.close),
                        color = Color(0xFF8A3040),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(title: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, fontWeight = FontWeight.SemiBold, color = Color(0xFF3B2B2B))
        Text(value, color = Color(0xFF737373))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AssignCareGuideDialog(
    careGuide: CareGuide,
    products: List<ProductResponse>,
    selectedProduct: ProductResponse?,
    isLoading: Boolean,
    errorMessage: String?,
    onProductSelected: (ProductResponse) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.assign_care_guide_title, careGuide.productName),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF3B2B2B)
                )

                var expanded by remember { mutableStateOf(false) }

                if (products.isEmpty()) {
                    OutlinedTextField(
                        value = stringResource(R.string.no_products_found),
                        onValueChange = {},
                        label = { Text(stringResource(R.string.select_product)) },
                        readOnly = true,
                        enabled = false,
                        shape = RoundedCornerShape(12.dp)
                    )
                } else {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedProduct?.name.orEmpty(),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.select_product)) },
                            placeholder = { Text(stringResource(R.string.select_product)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            products.forEach { product ->
                                DropdownMenuItem(
                                    text = { Text(product.name) },
                                    onClick = {
                                        onProductSelected(product)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                if (!errorMessage.isNullOrBlank()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(stringResource(R.string.cancel))
                    }

                    Button(
                        onClick = onConfirm,
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4A1B2A),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(stringResource(R.string.assign))
                        }
                    }
                }
            }
        }
    }
}
