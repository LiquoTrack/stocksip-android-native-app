package com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.storage.components

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
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductResponse

/**
 * A composable function that displays a product card with its image, name, and price.
 *
 * @param product The product to be displayed in the card.
 * @param onClick The callback to be invoked when the card is clicked. This typically navigates to the product details screen.
 */
@Composable
fun ProductCard(
    product: ProductResponse,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .height(200.dp)
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
            AsyncImage(
                model = product.imageUrl,
                contentDescription = "Product Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A1B2A),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        Text(
                            text = "Stock: ",
                            fontSize = 12.sp,
                            color = Color(0xFF9E9E9E),
                        )
                        Text(
                            text = product.totalStockInWarehouse.toString(),
                            fontSize = 12.sp,
                            color = Color(0xFF4A1B2A),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row {
                        Text(
                            text = "Price: ",
                            fontSize = 12.sp,
                            color = Color(0xFF9E9E9E),
                        )

                        Text(
                            text = "${product.currencyCode} ${product.unitPrice}",
                            fontSize = 12.sp,
                            color = Color(0xFF4A1B2A),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}