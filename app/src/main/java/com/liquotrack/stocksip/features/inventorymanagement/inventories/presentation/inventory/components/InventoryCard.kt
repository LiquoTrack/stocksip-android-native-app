package com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventory.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryResponse

/**
 * A composable function that displays an inventory card with its image, name, stock quantity, unit price, and expiration date.
 *
 * @param inventory The inventory item to be displayed in the card.
 * @param onClick The callback to be invoked when the card is clicked. This typically navigates to the inventory details screen.
 */
@Composable
fun InventoryCard(
    inventory: InventoryResponse,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = onClick
    ) {
        Column {
            // Product Image
            AsyncImage(
                model = inventory.imageUrl,
                contentDescription = "Inventory Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)

            // Important info
            Column(modifier = Modifier.padding(12.dp)) {
                // Product Name
                Text(
                    text = inventory.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A1B2A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Stock and Unit Price
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Stock Quantity
                    Row {
                        Text(
                            text = "Stock: ",
                            fontSize = 12.sp,
                            color = Color(0xFF9E9E9E)
                        )
                        Text(
                            text = inventory.quantity.toString(),
                            fontSize = 12.sp,
                            color = Color(0xFF4A1B2A),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Unit Price
                    Row {
                        Text(
                            text = "Price: ",
                            fontSize = 12.sp,
                            color = Color(0xFF9E9E9E)
                        )
                        Text(
                            text = "${inventory.moneyCode} ${inventory.unitPrice}",
                            fontSize = 12.sp,
                            color = Color(0xFF4A1B2A),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Expiration Date, if available
                inventory.expirationDate?.let { date ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Expires: $date",
                        fontSize = 12.sp,
                        color = Color(0xFF9E9E9E)
                    )
                }
            }
        }
    }
}