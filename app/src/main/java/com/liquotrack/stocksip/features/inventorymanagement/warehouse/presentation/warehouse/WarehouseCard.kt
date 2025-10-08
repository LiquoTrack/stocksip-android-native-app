package com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.Warehouse

@Composable
fun WarehouseCard(
    warehouse: Warehouse,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        AsyncImage(
            model = warehouse.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .height(96.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        )

        Text(
            text = warehouse.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "${warehouse.city}, ${warehouse.country}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}