package com.liquotrack.stocksip.features.procurementordering.purchaseorders.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import com.liquotrack.stocksip.shared.ui.components.TopBarWithBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val subTotal by viewModel.subTotal.collectAsState()
    val total by viewModel.total.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4ECEC))
    ) {
        TopBarWithBack(
            title = "New Order",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Products in your cart",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF8B4C5C),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Your cart is empty",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D1B2E)
                        )
                        Text("Add products to get started", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemCard(
                            item = item,
                            onIncrease = { viewModel.increaseQuantity(item) },
                            onDecrease = { viewModel.decreaseQuantity(item) },
                            onRemove = { viewModel.removeItem(item) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }

                TotalsSection(subTotal, total)
                Spacer(modifier = Modifier.height(16.dp))
                NextButton(isLoading = isLoading, onNextClick = onNextClick)
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.size(96.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F4F4))
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val imageUrl = item.imageUrl ?: ""
                    if (imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = item.productName,
                            modifier = Modifier.fillMaxSize().padding(8.dp),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Text("No Image", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(item.productName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D1B2E))
                    Text("Price: $${"%.2f".format(item.unitPrice)}", fontSize = 14.sp, color = Color(0xFF666666))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onDecrease,
                            modifier = Modifier.size(36.dp).background(Color(0xFFF0E6E8), CircleShape)
                        ) { Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color(0xFF8B4C5C), modifier = Modifier.size(18.dp)) }

                        Text(item.quantity.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D1B2E))

                        IconButton(
                            onClick = onIncrease,
                            modifier = Modifier.size(36.dp).background(Color(0xFF8B4C5C), CircleShape)
                        ) { Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.White, modifier = Modifier.size(18.dp)) }
                    }

                    Text("Subtotal: $${"%.2f".format(item.unitPrice * item.quantity)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B4C5C))
                }
            }

            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(36.dp).background(Color(0xFFFFEBEE), CircleShape)
            ) { Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFD32F2F), modifier = Modifier.size(20.dp)) }
        }
    }
}

@Composable
fun TotalsSection(subTotal: String, total: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Sub Total:", fontSize = 16.sp, color = Color(0xFF666666))
                Text(subTotal, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D1B2E))
            }

            Divider(color = Color(0xFFE0E0E0))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total:", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D1B2E))
                Text(total, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF8B4C5C))
            }
        }
    }
}

@Composable
fun NextButton(isLoading: Boolean, onNextClick: () -> Unit) {
    Button(
        onClick = onNextClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(8.dp, RoundedCornerShape(28.dp), spotColor = Color(0xFF8B4C5C)),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D1B2E)),
        shape = RoundedCornerShape(28.dp),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
        } else {
            Text("Next", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
