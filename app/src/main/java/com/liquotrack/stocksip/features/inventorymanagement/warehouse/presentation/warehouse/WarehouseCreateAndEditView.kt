package com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseResponse
import com.liquotrack.stocksip.shared.ui.components.TopAppBar
import java.io.File
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse.components.CustomTextField
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse.components.CustomDoubleTextField

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
    val imageFile by viewModel.imageFile.collectAsState()

    val isEditMode = warehouseId != null && warehouseId!= "new" && warehouseId.isNotBlank()

    val showErrors by remember { mutableStateOf(false) }

    val isValidFormat = name.isNotBlank() &&
        street.isNotBlank() &&
        city.isNotBlank() &&
        district.isNotBlank() &&
        postalCode.isNotBlank() &&
        country.isNotBlank() &&
        capacity > 0.0 &&
        minTemp < maxTemp

    LaunchedEffect(warehouse) {
        warehouse?.let {
            viewModel.updateWarehouseName(it.name)
            viewModel.updateStreet(it.street)
            viewModel.updateCity(it.city)
            viewModel.updateDistrict(it.district)
            viewModel.updatePostalCode(it.postalCode)
            viewModel.updateCountry(it.country)
            viewModel.updateCapacity(it.capacity)
            viewModel.updateMinTemp(it.temperatureMin)
            viewModel.updateMaxTemp(it.temperatureMax)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .background(Color(0xFFF4ECEC))
        ) {

            // Image Selection Section
            ImageSelectionSection(
                selectedImage = imageFile,
                onImageSelected = { file ->
                    viewModel.updateImageFile(file)
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
                    label = "Warehouse Name",
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

            Button(
                onClick = {
                    viewModel.saveWarehouse()
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2B000D)
                ),
                enabled = isValidFormat
            ) {
                Text(
                    text = if (isEditMode) "Update Warehouse" else "Add Warehouse",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun ImageSelectionSection(
    selectedImage: File?,
    onImageSelected: (File) -> Unit
) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = Color.LightGray,
                    shape = CircleShape
                )
                .clickable {

                }
        ) {
            if (selectedImage != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(selectedImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Selected warehouse image",
                    modifier = Modifier
                        .size(120.dp),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Select image",
                        tint = Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Select Image",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}