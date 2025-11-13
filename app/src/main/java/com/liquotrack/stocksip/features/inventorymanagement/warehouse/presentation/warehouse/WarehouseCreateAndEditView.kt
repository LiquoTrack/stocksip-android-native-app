package com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseResponse
import java.io.File
import com.liquotrack.stocksip.shared.presentation.components.CustomTextField
import com.liquotrack.stocksip.shared.presentation.components.CustomDoubleTextField
import androidx.core.net.toUri
import com.liquotrack.stocksip.shared.presentation.components.ImageSelectionSection
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.shared.ui.components.TopBarWithBack
import kotlinx.coroutines.launch
import com.liquotrack.stocksip.R

@Composable
fun WarehouseCreateAndEditView(
    viewModel: WarehouseViewModel = hiltViewModel(),
    warehouseId: String?,
    warehouse: WarehouseResponse? = null,
    onNavigateBack: () -> Unit
) {
    val name by viewModel.warehouseName.collectAsState()
    val street by viewModel.street.collectAsState()
    val city by viewModel.cityState.collectAsState()
    val district by viewModel.district.collectAsState()
    val postalCode by viewModel.postalCode.collectAsState()
    val country by viewModel.country.collectAsState()
    val capacity by viewModel.capacity.collectAsState()
    val minTemp by viewModel.minTemp.collectAsState()
    val maxTemp by viewModel.maxTemp.collectAsState()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageFile by remember { mutableStateOf<File?>(null) }

    val isEditMode = warehouseId != null && warehouseId != "new" && warehouseId.isNotBlank()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedWarehouse by viewModel.selectedWarehouse.collectAsState()
    val temperatureError by viewModel.temperatureError.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val bg = Color(0xFFF4ECEC)

    val isValidFormat = name.isNotBlank() &&
            street.isNotBlank() &&
            city.isNotBlank() &&
            district.isNotBlank() &&
            postalCode.isNotBlank() &&
            country.isNotBlank() &&
            capacity > 0.0 &&
            minTemp < maxTemp

    LaunchedEffect(warehouseId) {
        if (isEditMode) {
            viewModel.getWarehouseById(warehouseId)
        }
    }

    LaunchedEffect(selectedWarehouse) {
        selectedWarehouse?.let { warehouse ->
            viewModel.loadWarehouseForEdit(warehouse)
            if (warehouse.imageUrl.isNotBlank()) {
                selectedImageUri = warehouse.imageUrl.toUri()
            }
        }
    }

    LaunchedEffect(temperatureError) {
        temperatureError?.let { error ->
            snackBarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.clearTemperatureError()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                currentRoute = "warehouse",
                onNavigate = {},
                onClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBarWithBack(
                    title = if (isEditMode)
                        stringResource(R.string.label_edit_warehouse)
                    else
                        stringResource(R.string.label_new_warehouse),
                    onBackClick = onNavigateBack,
                    actions = {
                        if (isEditMode) {
                            IconButton(onClick = { viewModel.showDeleteConfirmationDialog(true) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.label_delete),
                                    tint = Color(0xFFE8B4BE)
                                )
                            }
                        }
                    }
                )
            },
            containerColor = bg,
            snackbarHost = { SnackbarHost(snackBarHostState) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(16.dp)
            ) {
                ImageSelectionSection(
                    selectedImage = selectedImageUri,
                    onImageSelected = { file, uri ->
                        selectedImageFile = file
                        selectedImageUri = uri
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomTextField(
                        value = name,
                        onValueChange = viewModel::updateWarehouseName,
                        label = stringResource(R.string.label_warehouse_name),
                        placeholder = "e.g., Main Warehouse"
                    )

                    CustomTextField(
                        value = street,
                        onValueChange = viewModel::updateStreet,
                        label = stringResource(R.string.label_warehouse_street),
                        placeholder = "e.g., 123 Main St"
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        CustomTextField(
                            value = city,
                            onValueChange = viewModel::updateCity,
                            label = stringResource(R.string.label_warehouse_city),
                            placeholder = "e.g., Lima",
                            modifier = Modifier.weight(1f)
                        )

                        CustomTextField(
                            value = district,
                            onValueChange = viewModel::updateDistrict,
                            label = stringResource(R.string.label_warehouse_district),
                            placeholder = "e.g., Chorrillos",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        CustomTextField(
                            value = postalCode,
                            onValueChange = viewModel::updatePostalCode,
                            label = stringResource(R.string.label_warehouse_postal_code),
                            placeholder = "e.g., 15063",
                            modifier = Modifier.weight(1f)
                        )

                        CustomTextField(
                            value = country,
                            onValueChange = viewModel::updateCountry,
                            label = stringResource(R.string.label_warehouse_country),
                            placeholder = "e.g., Perú",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    CustomDoubleTextField(
                        value = capacity,
                        onValueChange = viewModel::updateCapacity,
                        label = stringResource(R.string.label_warehouse_capacity),
                        placeholder = "e.g., 5000.0",
                        keyboardType = KeyboardType.Decimal
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        CustomDoubleTextField(
                            value = minTemp,
                            onValueChange = viewModel::updateMinTemp,
                            label = stringResource(R.string.label_warehouse_min_temperature),
                            placeholder = "e.g., -5.0",
                            modifier = Modifier.weight(1f),
                            keyboardType = KeyboardType.Number,
                            showError = temperatureError != null
                        )

                        CustomDoubleTextField(
                            value = maxTemp,
                            onValueChange = viewModel::updateMaxTemp,
                            label = stringResource(R.string.label_warehouse_max_temperature),
                            placeholder = "e.g., 25.0",
                            modifier = Modifier.weight(1f),
                            keyboardType = KeyboardType.Number,
                            showError = temperatureError != null
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        viewModel.updateImageFile(selectedImageFile)
                        viewModel.saveWarehouse(
                            isEditing = isEditMode,
                            warehouseId = warehouseId,
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
                            text = if (isEditMode)
                                stringResource(R.string.label_update_warehouse)
                            else
                                stringResource(R.string.label_add_warehouse),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            if (viewModel.showDeleteDialog.collectAsState().value) {
                AlertDialog(
                    onDismissRequest = { viewModel.showDeleteConfirmationDialog(false) },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.showDeleteConfirmationDialog(false)
                            warehouseId?.let { id ->
                                viewModel.deleteWarehouseById(id) {
                                    onNavigateBack()
                                }
                            }
                        }) {
                            Text(
                                stringResource(R.string.label_delete),
                                color = Color.Red
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.showDeleteConfirmationDialog(false) }) {
                            Text(stringResource(R.string.label_cancel))
                        }
                    },
                    title = { Text(stringResource(R.string.label_delete_warehouse)) },
                    text = { Text(stringResource(R.string.label_delete_confirmation_message)) },
                    containerColor = Color.White
                )
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }
}
