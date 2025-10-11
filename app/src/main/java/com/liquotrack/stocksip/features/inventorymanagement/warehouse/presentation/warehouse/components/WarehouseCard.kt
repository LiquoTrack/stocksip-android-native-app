package com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseResponse

@Composable
fun WarehouseCard(
    warehouse: WarehouseResponse,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        AsyncImage(
            model = warehouse.imageUrl,
            contentDescription = "Warehouse Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Text(
            text = warehouse.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "${warehouse.city}, ${warehouse.country}",
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}