package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation.storeownercatalogs.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.shared.ui.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierCatalogListScreen(
    supplierId: String,
    onBackClick: () -> Unit,
    onCatalogSelected: (String) -> Unit, // catalogId
    viewModel: SupplierCatalogViewModel = hiltViewModel()
) {
    val supplierInfo by viewModel.supplierInfo.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(supplierId) {
        viewModel.loadSupplierCatalogs(supplierId)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Supplier Catalogs",
                showBackButton = true,
                onNavigationClick = onBackClick
            )
        },
        containerColor = Color(0xFFF8EFEF)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF8B4C5C)
                    )
                }

                error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error loading catalogs", color = Color.Red)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(error ?: "", color = Color.Gray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadSupplierCatalogs(supplierId) }) {
                            Text("Retry")
                        }
                    }
                }

                supplierInfo != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Supplier info card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7E7E8))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    supplierInfo!!.account.business.businessName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "${supplierInfo!!.catalogs.size} catalog${if (supplierInfo!!.catalogs.size != 1) "s" else ""} available",
                                    color = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Catalogs list
                        if (supplierInfo!!.catalogs.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "No catalogs available",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                        } else {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(supplierInfo!!.catalogs.size) { index ->
                                    val catalog = supplierInfo!!.catalogs[index]
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onCatalogSelected(catalog.id) },
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    catalog.name,
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 16.sp
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    catalog.description,
                                                    color = Color.Gray,
                                                    fontSize = 14.sp,
                                                    maxLines = 2
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column(horizontalAlignment = Alignment.End) {
                                                Text(
                                                    "${catalog.catalogItems.size}",
                                                    color = Color(0xFF8B4C5C),
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 18.sp
                                                )
                                                Text(
                                                    "products",
                                                    color = Color(0xFF8B4C5C),
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}