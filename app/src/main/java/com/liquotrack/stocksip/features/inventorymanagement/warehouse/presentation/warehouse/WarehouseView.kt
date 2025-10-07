package com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.shared.ui.theme.onSurfaceLightMediumContrast
import com.liquotrack.stocksip.shared.ui.theme.onTertiaryContainerLightMediumContrast
import com.liquotrack.stocksip.shared.ui.theme.secondaryLight


@Composable
fun WarehouseView(
    modifier: Modifier = Modifier,
    viewModel: WarehouseViewModel = hiltViewModel()
) {
    val warehouses by viewModel.warehouses.collectAsState()

    Column(modifier =
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.height(64.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Warehouses",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = secondaryLight,
                modifier = Modifier.padding(start = 20.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(onTertiaryContainerLightMediumContrast)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Warehouse,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(end = 8.dp),
                        tint = onSurfaceLightMediumContrast
                    )
                    Text(
                        "Max. Allowed: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = onSurfaceLightMediumContrast
                    )
                    Text(
                        "10",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = onSurfaceLightMediumContrast
                    )
                }

                Button(
                    onClick = { /* TODO: Handle add warehouse action */ },
                    modifier = Modifier.height(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = onSurfaceLightMediumContrast,
                        contentColor = onTertiaryContainerLightMediumContrast
                    )
                ) {
                    Text(" + New Warehouse")
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(warehouses) { warehouse ->
                WarehouseCard(
                    warehouse = warehouse,
                    onClick = { }
                )
            }
        }
    }
}