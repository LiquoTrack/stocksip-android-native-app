package com.liquotrack.stocksip.features.ordermanagement.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.shared.ui.components.DrawerScaffold

data class SupplierOrderItemUi(
    val id: String,
    val title: String,
    val priceLabel: String,
    val quantity: Int,
    val status: String,
    val ownerEmail: String,
    val ownerPhone: String,
    val generatedAt: String
)

@Composable
fun SupplierSalesOrdersView(
    onNavigate: (String) -> Unit,
    onChangeStatus: (SupplierOrderItemUi) -> Unit,
    onLogout: () -> Unit
) {
    val bg = Color(0xFFF4ECEC)
    var showStatusDialog by remember { mutableStateOf(false) }
    var selectedOrder by remember { mutableStateOf<SupplierOrderItemUi?>(null) }
    var selectedStatus by remember { mutableStateOf<String?>(null) }
    val viewModel: SalesOrdersViewModel = hiltViewModel()
    val orders by viewModel.supplierOrders.collectAsState()

    DrawerScaffold(
        title = stringResource(id = R.string.orders_title),
        currentRoute = "orders_supplier",
        onNavigate = onNavigate,
        onLogout = onLogout,
        backgroundColor = bg
    ) { padding ->
        LaunchedEffect(Unit) { viewModel.loadSupplierOrders() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(bg)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                items(orders) { order ->
                    SupplierOrderCard(
                        order = order,
                        onChangeStatus = { clickedOrder ->
                            selectedOrder = clickedOrder
                            selectedStatus = clickedOrder.status
                            showStatusDialog = true
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    if (showStatusDialog && selectedOrder != null) {
        SupplierSalesOrderChangeStatus(
            isVisible = showStatusDialog,
            currentStatus = selectedStatus ?: selectedOrder!!.status,
            onSelect = { option ->
                selectedStatus = option
                selectedOrder = selectedOrder?.copy(status = option)
                selectedOrder?.let { viewModel.updateOrderStatus(it.id, option) }
            },
            onDismiss = { showStatusDialog = false }
        )
    }
}

@Composable
fun SupplierOrderCard(order: SupplierOrderItemUi, onChangeStatus: (SupplierOrderItemUi) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFCF4EF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = order.id,
                color = Color(0xFF9A9A9A),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = order.title,
                color = Color(0xFF4A1B2A),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.price_label, order.priceLabel),
                color = Color(0xFF9A6E6E),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(id = R.string.quantity_label, order.quantity),
                color = Color(0xFFDE9AA7),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { onChangeStatus(order) },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE5E5E5),
                        contentColor = Color(0xFF6B6B6B)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(text = stringResource(id = R.string.change_status), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Surface(
                    color = Color(0xFF9CF2CC),
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.owner_email_label, order.ownerEmail),
                color = Color(0xFF9A9A9A),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.owner_phone_label, order.ownerPhone),
                color = Color(0xFF9A9A9A),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.generated_at_label, order.generatedAt),
                color = Color(0xFF9A6E6E),
                fontSize = 14.sp
            )
        }
    }
}
