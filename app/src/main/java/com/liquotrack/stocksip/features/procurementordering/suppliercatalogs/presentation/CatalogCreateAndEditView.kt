package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.liquotrack.stocksip.shared.ui.components.TopBarWithBack
import kotlinx.coroutines.launch

/**
 * Unified screen for creating and editing catalogs
 * @param isEditMode true for editing existing catalog, false for creating new one
 * @param catalogId ID of catalog being edited (null when creating)
 * @param initialName Initial catalog name (for edit mode)
 * @param initialDescription Initial catalog description (for edit mode)
 * @param initialItems Initial catalog items with selection state (for edit mode)
 * @param initialPublishStatus Initial publish status (for edit mode)
 * @param onBack Callback for back navigation
 * @param onSave Callback for save action (add or update)
 * @param onDelete Callback for delete action (only in edit mode)
 */
@Composable
fun CatalogCreateAndEditScreen(
    isEditMode: Boolean = false,
    catalogId: String? = null,
    onBack: () -> Unit,
    viewModel: CatalogViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val catalogs by viewModel.catalogs.collectAsStateWithLifecycle()
    val selectedCatalog = catalogs.find { it.id == catalogId }

    var catalogName by remember { mutableStateOf(selectedCatalog?.name ?: "") }
    var catalogDescription by remember { mutableStateOf(selectedCatalog?.description ?: "") }
    var contactEmail by remember { mutableStateOf(selectedCatalog?.contactEmail ?: "") }
    var isPublished by remember { mutableStateOf(selectedCatalog?.isPublished ?: false) }

    val items = selectedCatalog?.catalogItems ?: emptyList()

    LaunchedEffect(Unit) {
        viewModel.loadCatalogsByAccount()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4ECEC))
    ) {
        TopBarWithBack(
            title = if (isEditMode) "Edit Catalog" else "New Catalog",
            onBackClick = onBack,
            actions = {
                if (isEditMode && catalogId != null) {
                    IconButton(onClick = {
                        Log.d("CATALOG", "Delete catalog $catalogId")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
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
            Text("Catalog info", color = Color(0xFFE8B4BE), fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = catalogName,
                onValueChange = { catalogName = it },
                placeholder = { Text("Name", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF8B4C5C)
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = catalogDescription,
                onValueChange = { catalogDescription = it },
                placeholder = { Text("Description", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF8B4C5C)
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = contactEmail,
                onValueChange = { contactEmail = it },
                placeholder = { Text("Contact Email", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF8B4C5C)
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(Modifier.height(24.dp))

            Text("Catalog items", color = Color(0xFFE8B4BE), fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (items.isEmpty()) {
                        Text("No products in this catalog", color = Color.Gray)
                    } else {
                        items.forEach { item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "${item.productName} — ${item.unitPrice}",
                                    color = Color.Black,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(Modifier.height(6.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

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

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    scope.launch {
                        if (isEditMode && catalogId != null) {
                            viewModel.updateCatalog(catalogId, catalogName, catalogDescription, contactEmail)
                            if (isPublished) viewModel.publishCatalog(catalogId)
                            else viewModel.unpublishCatalog(catalogId)
                        } else {
                            viewModel.createCatalog(catalogName, catalogDescription, contactEmail)
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
                    if (isEditMode) "Save" else "Add",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}