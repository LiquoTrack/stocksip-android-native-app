package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation.storeownercatalogs.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.liquotrack.stocksip.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogItemDetailScreen(
    viewModel: CatalogItemDetailViewModel,
    onBackClick: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val catalogItem by viewModel.catalogItem.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var quantity by remember { mutableIntStateOf(1) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    if (catalogItem == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF8B4C5C))
        }
        return
    }

    val item = catalogItem!!

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = {
                Text(
                    stringResource(R.string.catalog_item_detail_added_title),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D1B2E)
                )
            },
            text = {
                Text(
                    stringResource(
                        R.string.catalog_item_detail_added_message,
                        quantity,
                        item.productName
                    ),
                    color = Color(0xFF666666)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        onNavigateToCart()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF8B4C5C)
                    )
                ) {
                    Text(
                        stringResource(R.string.catalog_item_detail_view_cart),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showSuccessDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF8B4C5C)
                    )
                ) {
                    Text(stringResource(R.string.catalog_item_detail_continue))
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2D1B2E),
                            Color(0xFF5C1F2E)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Product Image Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .size(240.dp)
                            .shadow(
                                elevation = 24.dp,
                                shape = RoundedCornerShape(24.dp),
                                spotColor = Color(0xFF8B4C5C)
                            ),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (item.imageUrl != null) {
                                AsyncImage(
                                    model = item.imageUrl,
                                    contentDescription = item.productName,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp),
                                    contentScale = ContentScale.Fit
                                )
                            } else {
                                Text(
                                    stringResource(R.string.catalog_item_detail_no_image),
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    val stockLabel = if (item.availableStock > 0)
                        stringResource(R.string.catalog_item_detail_in_stock)
                    else
                        stringResource(R.string.catalog_item_detail_out_of_stock)

                    val stockColor =
                        if (item.availableStock > 0) Color(0xFF4CAF50) else Color(0xFFFF5252)

                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = stockColor
                    ) {
                        Text(
                            stockLabel,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Content Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-30).dp),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F4F4))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Text(
                            item.productName,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D1B2E),
                            lineHeight = 34.sp
                        )

                        Spacer(Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                item.unitPrice
                                    ?: stringResource(R.string.catalog_item_detail_price_not_available),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF8B4C5C)
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        // Info cards
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InfoCard(
                                icon = Icons.Outlined.Inventory,
                                label = stringResource(R.string.catalog_item_detail_stock_label),
                                value = stringResource(
                                    R.string.catalog_item_detail_stock_value,
                                    item.availableStock
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            InfoCard(
                                icon = Icons.Outlined.LocalOffer,
                                label = stringResource(R.string.catalog_item_detail_price_label),
                                value = item.unitPrice
                                    ?: stringResource(R.string.catalog_item_detail_price_not_available),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        Text(
                            stringResource(R.string.catalog_item_detail_about_title),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D1B2E)
                        )

                        Spacer(Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                stringResource(R.string.catalog_item_detail_about_text),
                                modifier = Modifier.padding(16.dp),
                                color = Color(0xFF666666),
                                fontSize = 15.sp,
                                lineHeight = 22.sp
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        Text(
                            stringResource(R.string.catalog_item_detail_select_quantity),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2D1B2E)
                        )

                        Spacer(Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { if (quantity > 1) quantity-- },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(Color(0xFFF0E6E8), CircleShape)
                                ) {
                                    Icon(
                                        Icons.Default.Remove,
                                        contentDescription = "Decrease",
                                        tint = Color(0xFF8B4C5C)
                                    )
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        quantity.toString(),
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2D1B2E)
                                    )
                                    Text(
                                        stringResource(R.string.catalog_item_detail_units),
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        if (quantity < item.availableStock) quantity++
                                    },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(Color(0xFF8B4C5C), CircleShape),
                                    enabled = quantity < item.availableStock
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Increase",
                                        tint = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(100.dp))
                    }
                }
            }
        }

        // Floating Add to Cart Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFFF8F4F4)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Button(
                onClick = {
                    viewModel.addToCart(item, quantity)
                    showSuccessDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(30.dp),
                        spotColor = Color(0xFF8B4C5C)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8B4C5C),
                    disabledContainerColor = Color.Gray
                ),
                shape = RoundedCornerShape(30.dp),
                enabled = item.availableStock > 0 && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(24.dp))
                        Text(
                            if (item.availableStock > 0)
                                stringResource(R.string.catalog_item_detail_add_to_cart, quantity)
                            else
                                stringResource(R.string.catalog_item_detail_out_of_stock),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF8B4C5C), modifier = Modifier.size(32.dp))
            Spacer(Modifier.height(8.dp))
            Text(label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D1B2E))
        }
    }
}
