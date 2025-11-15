package com.liquotrack.stocksip.features.ordermanagement.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderResponse
import com.liquotrack.stocksip.shared.ui.components.DrawerScaffold

@Composable
fun SupplierSalesOrdersView(
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    purchaseOrderId: String? = null,
) {
    val bg = Color(0xFFF4ECEC)
    val viewModel: SalesOrdersViewModel = hiltViewModel()
    val orders by viewModel.salesOrders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(purchaseOrderId) {
        if (purchaseOrderId != null) {
            viewModel.createSalesOrderFromProcurement(purchaseOrderId)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getSalesOrdersForAccount()
    }

    DrawerScaffold(
        title = stringResource(id = R.string.orders_title),
        currentRoute = "orders_supplier",
        onNavigate = onNavigate,
        onLogout = onLogout,
        backgroundColor = bg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(bg)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF6B6B6B))
                    }
                }
                error != null -> {
                    Text(
                        text = error ?: "Unknown error",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                orders.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        items(orders) { order ->
                            SupplierOrderCard(order = order)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
                else -> {
                    Text(
                        text = "No orders found.",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun SupplierOrderCard(order: SalesOrderResponse) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFCF4EF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = order.orderCode,
                color = Color(0xFF4A1B2A),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            val total = order.items.sumOf { it.unitPrice * it.quantityToSell }
            val firstCurrency = order.items.firstOrNull()?.currency ?: "PEN"
            val priceText = when (firstCurrency.uppercase()) {
                "PEN", "S/.", "SOL", "SOLES" -> "S/. %.2f".format(total)
                "USD", "$" -> "$. %.2f".format(total)
                else -> "$firstCurrency %.2f".format(total)
            }

            Text(
                text = stringResource(id = R.string.price_label, priceText),
                color = Color(0xFF9A6E6E),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(id = R.string.quantity_label, order.items.sumOf { it.quantityToSell }),
                color = Color(0xFFDE9AA7),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = when (order.status.uppercase()) {
                    "CONFIRM", "CONFIRMED" -> Color(0xFF9CF2CC)
                    "CANCEL", "CANCELED" -> Color(0xFFF2B9B9)
                    else -> Color(0xFFF2E49C)
                },
                shape = MaterialTheme.shapes.small
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (order.status.uppercase()) {
                            "PENDING" -> stringResource(id = R.string.status_pending)
                            "CONFIRM", "CONFIRMED" -> stringResource(id = R.string.status_confirm)
                            "CANCEL", "CANCELED" -> stringResource(id = R.string.status_cancel)
                            else -> order.status
                        },
                        color = Color(0xFF0B6F45),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.generated_at_label, order.receiptDate ?: "-"),
                color = Color(0xFF9A6E6E),
                fontSize = 14.sp
            )
        }
    }
}
