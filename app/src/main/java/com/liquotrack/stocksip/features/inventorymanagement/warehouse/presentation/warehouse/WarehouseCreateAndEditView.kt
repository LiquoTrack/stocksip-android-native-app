package com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseResponse
import com.liquotrack.stocksip.shared.ui.components.TopAppBar
import java.io.File
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse.components.CustomTextField
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse.components.CustomDoubleTextField
import androidx.core.net.toUri
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = if (isEditMode) stringResource(R.string.label_edit_warehouse) else stringResource(R.string.label_new_warehouse),
                onBackClick = onNavigateBack,
                isEditMode = isEditMode,
                onDeleteClick = {
                    if (isEditMode) {
                        viewModel.showDeleteConfirmationDialog(true)
                    }
                }
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
        },
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

                ImageSelectionSection(
                    selectedImage = selectedImageUri,
                    onImageSelected = { file, uri ->
                        selectedImageFile = file
                        selectedImageUri = uri
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Form Fields
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Warehouse Name
                    CustomTextField(
                        value = name,
                        onValueChange = viewModel::updateWarehouseName,
                        label = stringResource(R.string.label_warehouse_name),
                        placeholder = "e.g., Main Warehouse"
                    )

                    // Street
                    CustomTextField(
                        value = street,
                        onValueChange = viewModel::updateStreet,
                        label = stringResource(R.string.label_warehouse_street),
                        placeholder = "e.g. 123 Main St"
                    )

                    // City and District in Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CustomTextField(
                            value = city,
                            onValueChange = viewModel::updateCity,
                            label = stringResource(R.string.label_warehouse_city),
                            placeholder = "eg. Lima",
                            modifier = Modifier.weight(1f)
                        )

                        CustomTextField(
                            value = district,
                            onValueChange = viewModel::updateDistrict,
                            label = stringResource(R.string.label_warehouse_district),
                            placeholder = "e.g. Chorrillos",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Postal Code and Country
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CustomTextField(
                            value = postalCode,
                            onValueChange = viewModel::updatePostalCode,
                            label = stringResource(R.string.label_warehouse_postal_code),
                            placeholder = "e.g. 15063",
                            modifier = Modifier.weight(1f)
                        )

                        CustomTextField(
                            value = country,
                            onValueChange = viewModel::updateCountry,
                            label = stringResource(R.string.label_warehouse_country),
                            placeholder = "e.g. Peru",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Capacity
                    CustomDoubleTextField(
                        value = capacity,
                        onValueChange = viewModel::updateCapacity,
                        label = stringResource(R.string.label_warehouse_capacity),
                        placeholder = "e.g. 5000.0",
                        keyboardType = KeyboardType.Decimal
                    )

                    // Temperature Range
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CustomDoubleTextField(
                            value = minTemp,
                            onValueChange = viewModel::updateMinTemp,
                            label = "${stringResource(R.string.label_warehouse_min_temperature)} (°C)",
                            placeholder = "e.g. -5.0",
                            modifier = Modifier.weight(1f),
                            keyboardType = KeyboardType.Number,
                            showError = temperatureError != null
                        )

                        CustomDoubleTextField(
                            value = maxTemp,
                            onValueChange = viewModel::updateMaxTemp,
                            label = "${stringResource(R.string.label_warehouse_max_temperature)} (°C)",
                            placeholder = "e.g. 25.0",
                            modifier = Modifier.weight(1f),
                            keyboardType = KeyboardType.Number,
                            showError = temperatureError != null
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Save button
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
                            text = if (isEditMode) stringResource(R.string.label_update_warehouse) else stringResource(R.string.label_add_warehouse),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Confirmation Dialog for Deletion
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
                            Text("Delete", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.showDeleteConfirmationDialog(false) }) {
                            Text("Cancel")
                        }
                    },
                    title = { Text("Delete Warehouse") },
                    text = { Text("Are you sure you want to delete this warehouse? This action is irreversible.") },
                    containerColor = Color.White
                )
            }

            // Overlay Loading Indicator
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

@Composable
fun ImageSelectionSection(
    selectedImage: Uri?,
    onImageSelected: (File?, Uri?) -> Unit
) {
    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val inputStream = context.contentResolver.openInputStream(it)
                val tempFile = File.createTempFile("warehouse_image", ".jpg", context.cacheDir)
                inputStream?.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                onImageSelected(tempFile, uri)
            } catch (e: Exception) {
                e.printStackTrace()
                onImageSelected(null, null)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray)
                .clickable { imagePicker.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImage != null) {
                AsyncImage(
                    model = selectedImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color.DarkGray
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.label_select_image),
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

