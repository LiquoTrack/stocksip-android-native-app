package com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.product.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.Product

/**
 * A composable function that displays a product card with its image, name, and price.
 *
 * @param product The product to be displayed in the card.
 * @param onClick The callback to be invoked when the card is clicked.
 */
@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        AsyncImage(
            model = product.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "Total Stock",
                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "${product.totalStockInWarehouse}",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.DarkGray)
            )
        }
    }
}