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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderResponse
import com.liquotrack.stocksip.shared.ui.components.DrawerScaffold
import com.liquotrack.stocksip.R

data class OrderItemUi(
    val id: String,
    val code: String,
    val title: String,
    val priceLabel: String,
    val quantity: Int,
    val status: String,
    val generatedAt: String
)

@Composable
fun SalesOrdersView(
    onNavigate: (String) -> Unit,
    onNewClick: () -> Unit,
    onLogout: () -> Unit
) {
    val bg = Color(0xFFF4ECEC)
    val viewModel: OwnerSalesOrdersViewModel = hiltViewModel()
    val ordersUi = viewModel.ownerOrdersUi.collectAsState()

    DrawerScaffold(
        title = stringResource(id = R.string.orders_title),
        currentRoute = "orders",
        onNavigate = onNavigate,
        onLogout = onLogout,
        backgroundColor = bg
    ) { padding ->
        LaunchedEffect(Unit) { viewModel.loadOwnerOrders() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(bg)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                NewPillButton(onClick = onNewClick)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = R.string.orders_recent),
                color = Color(0xFFD88492),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                items(ordersUi.value) { item ->
                    OwnerOrderCard(item)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun NewPillButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF3C0F1E),
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .height(44.dp)
    ) {
        Text(text = stringResource(id = R.string.new_order), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun OwnerOrderCard(item: OrderItemUi) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFCF4EF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "#" + item.code,
                color = Color(0xFF9A9A9A),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.title,
                color = Color(0xFF4A1B2A),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.price_label, item.priceLabel),
                color = Color(0xFFD88492),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(id = R.string.quantity_label, item.quantity),
                color = Color(0xFFD88492),
                fontSize = 16.sp
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.generated_at_label, item.generatedAt),
                    color = Color(0xFF9A6E6E),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Surface(
                    color = Color(0xFFFFE28F),
                    shape = MaterialTheme.shapes.small
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (item.status.uppercase()) {
                                "PROCESSING" -> stringResource(id = R.string.sent_status)
                                else -> item.status
                            },
                            color = Color(0xFF6A4E00),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}