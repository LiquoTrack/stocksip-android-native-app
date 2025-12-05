package com.liquotrack.stocksip.features.ordermanagement.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.core.navigation.Route
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.presentation.OrderItemUi
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.presentation.PurchaseOrdersViewModel
import com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.presentation.account.AccountViewModel
import com.liquotrack.stocksip.shared.ui.components.DrawerScaffold

@Composable
fun PurchaseOrdersView(
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    navToCreate: () -> Unit
) {
    val bg = Color(0xFFF8F3F2)
    val viewModel: PurchaseOrdersViewModel = hiltViewModel()
    val accountViewModel: AccountViewModel = hiltViewModel()
    val ordersUi by viewModel.ordersUi.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val createdId by viewModel.createdPurchaseOrderId.collectAsState()
    val userRole by accountViewModel.accountRole.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadOrders()
        // Ensure role is loaded from storage on first composition
        if (userRole == null) {
            accountViewModel.loadAccountRoleFromStorage()
        }
    }

    LaunchedEffect(createdId) {
        if (!createdId.isNullOrEmpty()) {
            println(">>> NAV: salesorders/create/$createdId")
            onNavigate("salesorders/create/$createdId")
        }
    }


    DrawerScaffold(
        title = stringResource(R.string.purchase_orders_title),
        currentRoute = Route.MakingOrders.route,
        onNavigate = onNavigate,
        onLogout = onLogout,
        backgroundColor = bg,
        userRole = userRole
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF4ECEC))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            HeaderSection(
                onNewClick = navToCreate,
                onReloadClick = { viewModel.loadOrders() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Crossfade(targetState = isLoading) { loading ->
                when {
                    loading -> LoadingSection()
                    ordersUi.isEmpty() -> EmptySection(navToCreate)
                    else -> OrdersList(ordersUi)
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    onNewClick: () -> Unit,
    onReloadClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.your_recent_orders_label),
            color = Color(0xFF3C0F1E),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onReloadClick,
                modifier = Modifier
                    .background(Color(0xFFD8B4B4), shape = MaterialTheme.shapes.small)
                    .size(42.dp)
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = stringResource(R.string.reload_orders), tint = Color.White)
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onNewClick,
                modifier = Modifier
                    .background(Color(0xFF3C0F1E), shape = MaterialTheme.shapes.small)
                    .size(42.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.new_order_btn), tint = Color.White)
            }
        }
    }
}

@Composable
private fun LoadingSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color(0xFF3C0F1E))
    }
}

@Composable
private fun EmptySection(onNewClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                stringResource(R.string.no_orders_yet_message),
                color = Color(0xFF9A6E6E),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onNewClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C0F1E))
            ) {
                Text(stringResource(R.string.create_order_button), color = Color.White)
            }
        }
    }
}

@Composable
private fun OrdersList(ordersUi: List<OrderItemUi>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(ordersUi) { item -> PurchaseOrderCard(item) }
    }
}

@Composable
fun PurchaseOrderCard(item: OrderItemUi) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBFA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "#${item.code}",
                color = Color(0xFF8E6A6A),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(10.dp))

            item.products.forEach { product ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    product.imageUrl?.let { imageUrl ->
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = product.name,
                            modifier = Modifier
                                .size(42.dp)
                                .background(Color(0xFFF2D1D1), shape = MaterialTheme.shapes.medium)
                                .padding(4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.name,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = Color(0xFF3C0F1E),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "x${product.quantity} • $${"%.2f".format(product.unitPrice)} c/u",
                            color = Color(0xFF9A6E6E),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                color = Color(0xFFE0C7C7)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.total_label, item.priceLabel),
                    color = Color(0xFFD88492),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = stringResource(R.string.quantity_label_c, item.quantity),
                    color = Color(0xFF9A6E6E),
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.generated_label, item.generatedAt),
                    color = Color(0xFFB9A5A5),
                    fontSize = 13.sp
                )

                Surface(
                    color = when (item.status.uppercase()) {
                        "CONFIRMED" -> Color(0xFFD8F5C5)
                        "PROCESSING" -> Color(0xFFFFE28F)
                        else -> Color(0xFFF2D1D1)
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = item.status,
                        color = Color(0xFF3C0F1E),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

