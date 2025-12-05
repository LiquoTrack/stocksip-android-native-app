package com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.shared.presentation.components.CustomTextField
import com.liquotrack.stocksip.shared.presentation.components.CustomDoubleTextField
import com.liquotrack.stocksip.shared.presentation.components.ImageSelectionSection
import com.liquotrack.stocksip.shared.ui.components.TopBarWithBack
import java.io.File

@Composable
fun WarehouseCreateAndEditView(
    viewModel: WarehouseViewModel = hiltViewModel(),
    warehouseId: String?,
    onNavigateBack: () -> Unit
) {
    // States
    val name by viewModel.warehouseName.collectAsState()
    val street by viewModel.street.collectAsState()
    val city by viewModel.cityState.collectAsState()
    val district by viewModel.district.collectAsState()
    val postalCode by viewModel.postalCode.collectAsState()
    val country by viewModel.country.collectAsState()
    val capacity by viewModel.capacity.collectAsState()
    val minTemp by viewModel.minTemp.collectAsState()
    val maxTemp by viewModel.maxTemp.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val temperatureError by viewModel.temperatureError.collectAsState()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageFile by remember { mutableStateOf<File?>(null) }

    val isEditMode = warehouseId != null && warehouseId != "new" && warehouseId.isNotBlank()
    val isValidFormat = name.isNotBlank() &&
            street.isNotBlank() &&
            city.isNotBlank() &&
            district.isNotBlank() &&
            postalCode.isNotBlank() &&
            country.isNotBlank() &&
            capacity > 0.0 &&
            minTemp < maxTemp

    val snackBarHostState = remember { SnackbarHostState() }

    // Load warehouse data if editing
    LaunchedEffect(warehouseId) {
        if (isEditMode) viewModel.getWarehouseById(warehouseId)
    }

    val selectedWarehouse by viewModel.selectedWarehouse.collectAsState()
    LaunchedEffect(selectedWarehouse) {
        selectedWarehouse?.let { warehouse ->
            viewModel.loadWarehouseForEdit(warehouse)
            if (warehouse.imageUrl.isNotBlank()) selectedImageUri = warehouse.imageUrl.toUri()
        }
    }

    LaunchedEffect(temperatureError) {
        temperatureError?.let { error ->
            snackBarHostState.showSnackbar(error)
            viewModel.clearTemperatureError()
        }
    }

    Scaffold(
        topBar = {
            TopBarWithBack(
                title = if (isEditMode) "Edit Warehouse" else "New Warehouse",
                onBackClick = onNavigateBack
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = Color(0xFFF4ECEC)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
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
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    CustomTextField(
                        value = name,
                        onValueChange = viewModel::updateWarehouseName,
                        label = "Warehouse Name",
                        placeholder = "e.g., Main Warehouse"
                    )

                    CustomTextField(
                        value = street,
                        onValueChange = viewModel::updateStreet,
                        label = "Street",
                        placeholder = "e.g., 123 Main St"
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        CustomTextField(
                            value = city,
                            onValueChange = viewModel::updateCity,
                            label = "City",
                            placeholder = "e.g., Lima",
                            modifier = Modifier.weight(1f)
                        )

                        CustomTextField(
                            value = district,
                            onValueChange = viewModel::updateDistrict,
                            label = "District",
                            placeholder = "e.g., Chorrillos",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        CustomTextField(
                            value = postalCode,
                            onValueChange = viewModel::updatePostalCode,
                            label = "Postal Code",
                            placeholder = "e.g., 15063",
                            modifier = Modifier.weight(1f)
                        )

                        CustomTextField(
                            value = country,
                            onValueChange = viewModel::updateCountry,
                            label = "Country",
                            placeholder = "e.g., Peru",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    CustomDoubleTextField(
                        value = capacity,
                        onValueChange = viewModel::updateCapacity,
                        label = "Capacity",
                        placeholder = "e.g., 1000.0"
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        CustomDoubleTextField(
                            value = minTemp,
                            onValueChange = viewModel::updateMinTemp,
                            label = "Min Temp",
                            placeholder = "e.g., 5.0",
                            modifier = Modifier.weight(1f),
                            showError = temperatureError != null
                        )

                        CustomDoubleTextField(
                            value = maxTemp,
                            onValueChange = viewModel::updateMaxTemp,
                            label = "Max Temp",
                            placeholder = "e.g., 25.0",
                            modifier = Modifier.weight(1f),
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
                            onSuccess = onNavigateBack
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    enabled = isValidFormat && !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B000D))
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (isEditMode) "Update Warehouse" else "Add Warehouse",
                            fontSize = 16.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
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
