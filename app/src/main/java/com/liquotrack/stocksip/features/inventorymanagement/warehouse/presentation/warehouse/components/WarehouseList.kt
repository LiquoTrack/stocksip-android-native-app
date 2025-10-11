package com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseResponse

@Composable
fun WarehouseList(
    warehouses: List<WarehouseResponse>,
    onClick: (WarehouseResponse) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        items(warehouses) { warehouse ->
            WarehouseCard(
                warehouse = warehouse,
                onClick = { onClick(warehouse) }
            )
        }
    }
}