package com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventory.components

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
import androidx.compose.material.icons.filled.Liquor
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryResponse

/**
 * Composable function to display a list of inventories in a grid format.
 * If the inventory list is empty, a placeholder message is shown.
 *
 * @param inventories List of InventoryResponse objects to be displayed.
 * @param onClick Lambda function to handle click events on individual inventory items.
 * @param modifier Modifier for styling the composable.
 */
@Composable
fun InventoryList(
    inventories: List<InventoryResponse>,
    onClick: (InventoryResponse) -> Unit,
    modifier: Modifier = Modifier
) {
    if (inventories.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Liquor,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "You don't have any product in this warehouse yet.",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tap the 'Create' button to add one!.",
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
            items(inventories) { inventoryItem ->
                InventoryCard (
                    inventory = inventoryItem,
                    onClick = { onClick(inventoryItem) }
                )
            }
        }
    }
}