package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation.storeownercatalogs.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.Catalog
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.CatalogItem
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.SupplierInfo
import com.liquotrack.stocksip.shared.ui.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogDetailViewScreen(
    catalogId: String,
    onBackClick: () -> Unit,
    onProductClick: (CatalogItem) -> Unit,
    viewModel: CatalogDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(catalogId) {
        viewModel.loadCatalog(catalogId)
    }

    val catalog by viewModel.catalog.collectAsState()
    val supplierInfo by viewModel.supplierInfo.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                title = catalog?.name ?: "Catalog Detail",
                showBackButton = true,
                onNavigationClick = onBackClick
            )
        },
        containerColor = Color(0xFFF8EFEF)
    ) { padding ->
        when {
            isLoading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF5C1F2E))
            }

            error != null -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = error ?: "Unknown error",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }

            catalog != null -> CatalogDetailContent(
                catalog = catalog!!,
                supplierInfo = supplierInfo,
                modifier = Modifier.padding(padding),
                onProductClick = onProductClick
            )
        }
    }
}

@Composable
private fun CatalogDetailContent(
    catalog: Catalog,
    supplierInfo: SupplierInfo?,
    modifier: Modifier = Modifier,
    onProductClick: (CatalogItem) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7E7E8))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val business = supplierInfo?.account?.business

                Text(
                    text = business?.businessName
                        ?: catalog.ownerAccount.ifEmpty { "Unknown Supplier" },
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text("Supplier", color = Color.Gray)
                Text(
                    text = catalog.contactEmail,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Available Products",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color(0xFF5C1F2E)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (catalog.catalogItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No products found", color = Color.Gray)
            }
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(catalog.catalogItems) { item ->
                    val formattedPrice = try {
                        val numericPrice =
                            item.unitPrice?.replace("[^0-9.]".toRegex(), "")?.toDoubleOrNull()
                        if (numericPrice != null) "$" + String.format("%.2f", numericPrice)
                        else "N/A"
                    } catch (e: Exception) {
                        "N/A"
                    }

                    ProductCard(
                        productName = item.productName,
                        price = formattedPrice,
                        productImageUrl = item.imageUrl,
                        onAddToCartClick = { onProductClick(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    productName: String,
    price: String,
    productImageUrl: String?,
    onAddToCartClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .background(Color.White),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF7E7E8)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(productImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = productName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = productName,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                maxLines = 2
            )
            Text(price, color = Color.Gray, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onAddToCartClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C1F2E)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("ADD TO CART", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}
