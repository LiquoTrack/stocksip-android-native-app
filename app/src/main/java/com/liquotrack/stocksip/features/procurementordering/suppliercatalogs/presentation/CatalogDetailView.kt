package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.liquotrack.stocksip.shared.ui.components.TopBarWithBack

@Composable
fun CatalogDetailScreen(
    catalogId: String,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    viewModel: CatalogViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }

    val selectedCatalog by viewModel.selectedCatalog.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(catalogId) {
        viewModel.loadCatalogDetail(catalogId)
    }

    val catalogName = selectedCatalog?.name ?: "Catalog Detail"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4ECEC))
    ) {
        TopBarWithBack(
            title = catalogName,
            onBackClick = onBack,
            actions = {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color(0xFF4A1B2A)
                    )
                }
            }
        )

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF5C1F2E))
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error loading catalog: $error",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
            }

            selectedCatalog == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No catalog found", color = Color.Gray)
                }
            }

            else -> {
                val filteredItems = selectedCatalog!!.catalogItems.filter {
                    it.productName.contains(searchQuery, ignoreCase = true)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search products", color = Color.Gray) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.Gray
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color(0xFF8B4C5C)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (filteredItems.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No products found", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredItems) { item ->
                                val formattedPrice = try {
                                    val numericPrice = item.unitPrice
                                        ?.toString()
                                        ?.replace("[^0-9.]".toRegex(), "")
                                        ?.toDoubleOrNull()
                                    if (numericPrice != null) "$" + String.format("%.2f", numericPrice)
                                    else "N/A"
                                } catch (e: Exception) {
                                    "N/A"
                                }

                                ProductCard(
                                    productName = item.productName,
                                    price = formattedPrice,
                                    imageUrl = item.imageUrl
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    productName: String,
    price: String,
    imageUrl: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image
            AsyncImage(
                model = imageUrl ?: "",
                contentDescription = productName,
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFFF4ECEC), RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    productName,
                    color = Color(0xFF8B4C5C),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    price,
                    color = Color(0xFF8B4C5C),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "See more",
                    color = Color(0xFFE8B4BE),
                    fontSize = 12.sp
                )
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    tint = Color(0xFFE8B4BE)
                )
            }
        }
    }
}
