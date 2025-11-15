package com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseResponse

@Composable
fun WarehouseList(
    warehouse: List<WarehouseResponse>,
    onClick: (WarehouseResponse) -> Unit,
    onEditClick : (WarehouseResponse) -> Unit,
    modifier: Modifier = Modifier
) {
    if (warehouse.isEmpty()) {

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Warehouse,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${stringResource(R.string.label_empty_warehouses)}.",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${stringResource(R.string.label_empty_warehouses_description)}.",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            items(warehouse) { warehouseItem ->
                WarehouseCard(
                    warehouse = warehouseItem,
                    onClick = { onClick(warehouseItem) },
                    onEditClick = { onEditClick(warehouseItem) }
                )
            }
        }
    }
}