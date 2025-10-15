package com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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

    val selectedWarehouse by viewModel.selectedWarehouse.collectAsState()

    LaunchedEffect(selectedWarehouse?.imageUrl) {
        if (isEditMode && selectedWarehouse?.imageUrl?.isNotBlank() == true) {
            selectedImageUri = selectedWarehouse!!.imageUrl.toUri()
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = if (isEditMode) "Edit Warehouse" else "New Warehouse",
                onBackClick = onNavigateBack
            )
        },
        containerColor = Color(0xFFF4ECEC)
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
                        label = "Name",
                        placeholder = "Enter warehouse name"
                    )

                    // Street
                    CustomTextField(
                        value = street,
                        onValueChange = viewModel::updateStreet,
                        label = "Street",
                        placeholder = "Enter street address"
                    )

                    // City and District in Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CustomTextField(
                            value = city,
                            onValueChange = viewModel::updateCity,
                            label = "City",
                            placeholder = "City",
                            modifier = Modifier.weight(1f)
                        )

                        CustomTextField(
                            value = district,
                            onValueChange = viewModel::updateDistrict,
                            label = "District",
                            placeholder = "District",
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
                            label = "Postal Code",
                            placeholder = "Postal code",
                            modifier = Modifier.weight(1f)
                        )

                        CustomTextField(
                            value = country,
                            onValueChange = viewModel::updateCountry,
                            label = "Country",
                            placeholder = "Country",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Capacity
                    CustomDoubleTextField(
                        value = capacity,
                        onValueChange = viewModel::updateCapacity,
                        label = "Capacity",
                        placeholder = "Enter capacity",
                        keyboardType = KeyboardType.Decimal
                    )

                    // Temperature Range
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CustomDoubleTextField(
                            value = minTemp,
                            onValueChange = viewModel::updateMinTemp,
                            label = "Min Temperature (°C)",
                            placeholder = "Min °C",
                            modifier = Modifier.weight(1f),
                            keyboardType = KeyboardType.Decimal
                        )

                        CustomDoubleTextField(
                            value = maxTemp,
                            onValueChange = viewModel::updateMaxTemp,
                            label = "Max Temperature (°C)",
                            placeholder = "Max °C",
                            modifier = Modifier.weight(1f),
                            keyboardType = KeyboardType.Decimal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Save button
                Button(
                    onClick = {
                        viewModel.updateImageFile(selectedImageFile)
                        viewModel.saveWarehouse {
                            onNavigateBack()
                        }

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
                            text = if (isEditMode) "Update Warehouse" else "Add Warehouse",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
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
                .size(160.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray)
                .clickable { imagePicker.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImage != null) {
                AsyncImage(
                    model = selectedImage,
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Select Image",
                    modifier = Modifier.size(48.dp),
                    tint = Color.DarkGray
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Select an image",
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

