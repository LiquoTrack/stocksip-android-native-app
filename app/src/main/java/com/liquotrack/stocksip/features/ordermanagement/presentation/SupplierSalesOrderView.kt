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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.liquotrack.stocksip.shared.ui.components.DrawerScaffold
import com.liquotrack.stocksip.features.authentication.login.presentation.login.LoginViewModel
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.shared.ui.components.TopBar
import kotlinx.coroutines.launch

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
    onLogout: () -> Unit = {},
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val bg = Color(0xFFF4ECEC)
    var showStatusDialog by remember { mutableStateOf(false) }
    var selectedOrder by remember { mutableStateOf<SupplierOrderItemUi?>(null) }
    var selectedStatus by remember { mutableStateOf<String?>(null) }
    val viewModel: SalesOrdersViewModel = hiltViewModel()
    val orders by viewModel.supplierOrders.collectAsState()

    DrawerScaffold(
        title = "Orders",
        currentRoute = "orders_supplier",
        onNavigate = onNavigate,
        backgroundColor = bg
    ) { padding ->
        LaunchedEffect(Unit) { viewModel.loadSupplierOrders() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isLoggedOut by loginViewModel.isLoggedOut.collectAsState()

    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            onLogout()
            loginViewModel.resetLogoutState()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                currentRoute = "making_orders",
                onNavigate = onNavigate,
                onClose = { scope.launch { drawerState.close() } },
                onLogout = { loginViewModel.logout() }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = "Orders",
                    showBackButton = false,
                    onNavigationClick = { scope.launch { drawerState.open() } }
                )
            },
            containerColor = bg
        ) { padding ->
            val orders = remember {
                listOf(
                    SupplierOrderItemUi(
                        id = "#OR001",
                        title = "Vino Blanco",
                        priceLabel = "S/. 50.00",
                        quantity = 1,
                        status = "Received",
                        ownerEmail = "janedoe@gmail.com",
                        ownerPhone = "987654321",
                        generatedAt = "2/9/2025s"
                    )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(bg)
                    .padding(horizontal = 16.dp)
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
                Spacer(modifier = Modifier.height(24.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    items(orders) { order ->
                        SupplierOrderCard(order = order, onChangeStatus = onChangeStatus)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
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
                text = "Price: ${'$'}{order.priceLabel}",
                color = Color(0xFF9A6E6E),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Quantity: ${order.quantity}",
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
                    Text(text = "Change Status", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Surface(
                    color = Color(0xFF9CF2CC),
                    shape = MaterialTheme.shapes.small
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = order.status,
                            color = Color(0xFF0B6F45),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Owner email: ${order.ownerEmail}",
                color = Color(0xFF9A9A9A),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Owner phone: ${order.ownerPhone}",
                color = Color(0xFF9A9A9A),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Generated at: ${order.generatedAt}",
                color = Color(0xFF9A6E6E),
                fontSize = 14.sp
            )
        }
    }
}