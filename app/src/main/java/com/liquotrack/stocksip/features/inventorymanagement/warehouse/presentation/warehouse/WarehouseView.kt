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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse.components.WarehouseList
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.shared.ui.components.TopBar
import com.liquotrack.stocksip.shared.ui.theme.onSurfaceLightMediumContrast
import com.liquotrack.stocksip.shared.ui.theme.onTertiaryContainerLightMediumContrast
import kotlinx.coroutines.launch


@Composable
fun WarehouseView(
    viewModel: WarehouseViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit = {}
) {
    val warehouses by viewModel.warehouses.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val backgroundColor  = Color(0xFFF4ECEC)

    LaunchedEffect(Unit) {
        viewModel.getAllWarehousesByAccountId()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                currentRoute = "warehouse",
                onNavigate = onNavigate,
                onClose = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = "Warehouses",
                    showBackButton = false,
                    onNavigationClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },
            containerColor = backgroundColor
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

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
                        Row(verticalAlignment = Alignment.CenterVertically) {
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
                            onClick = {
                                onNavigate("warehouse_create_edit/new")
                            },
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

                WarehouseList(
                    warehouse = warehouses,
                    onClick = { warehouse ->
                        onNavigate("warehouse_details/${warehouse.id}")
                    },
                    onEditClick = { warehouse ->
                        onNavigate("warehouse_create_edit/${warehouse.id}")
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                )
            }
        }
    }
}