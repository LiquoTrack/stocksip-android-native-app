package com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.features.authentication.login.presentation.login.LoginViewModel
import com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventory.components.InventoryList
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.shared.ui.components.TopBar
import com.liquotrack.stocksip.shared.ui.theme.onSurfaceLightMediumContrast
import com.liquotrack.stocksip.shared.ui.theme.onTertiaryContainerLightMediumContrast
import kotlinx.coroutines.launch

/**
 * Composable function to display the Inventory View.
 *
 * This view includes a navigation drawer, a top bar, and a list of inventories.
 * It also handles user logout and navigation events.
 *
 * @param viewModel The ViewModel for managing inventory data. Defaults to Hilt-injected InventoryViewModel.
 * @param loginViewModel The ViewModel for managing login state. Defaults to Hilt-injected LoginViewModel.
 * @param warehouseId The ID of the warehouse to fetch inventories for.
 * @param onNavigate A lambda function to handle navigation events.
 * @param onLogout A lambda function to handle user logout events.
 */
@Composable
fun InventoryView(
    viewModel: InventoryViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    warehouseId: String? = null,
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {

    val inventories by viewModel.inventories.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isLoggedOut by loginViewModel.isLoggedOut.collectAsState()

    val backgroundColor  = Color(0xFFF4ECEC)

    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            onLogout()
            loginViewModel.resetLogoutState()
        }
    }

    LaunchedEffect(Unit) {
        if (warehouseId != null) {
            viewModel.getAllInventoriesByWarehouseId(warehouseId)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                currentRoute = "warehouse_inventory",
                onNavigate = onNavigate,
                onClose = {
                    scope.launch { drawerState.close() }
                },
                onLogout = {
                    loginViewModel.logout()
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = "Inventory",
                    showBackButton = false,
                    onNavigationClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },
            containerColor = backgroundColor
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
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
                        // Inventory Addition Button
                        // Navigates to the inventory addition screen for the specified warehouse
                        Button(
                            onClick = {
                                onNavigate("inventory_addition/${warehouseId}")
                            },
                            modifier = Modifier.height(36.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = onSurfaceLightMediumContrast,
                                contentColor = onTertiaryContainerLightMediumContrast
                            )
                        ) {
                            Text(" + Add Products")
                        }

                        // Inventory Subtrack Button
                        // Navigates to the inventory subtrack screen for the specified warehouse
                        Button(
                            onClick = {
                                onNavigate("inventory_subtrack/${warehouseId}")
                            },
                            modifier = Modifier.height(36.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = onSurfaceLightMediumContrast,
                                contentColor = onTertiaryContainerLightMediumContrast
                            )
                        ) {
                            Text(" - Subtrack Products")
                        }

                        // Inventory Transfer Button
                        // Navigates to the inventory transfer screen for the specified warehouse
                        Button(
                            onClick = {
                                onNavigate("inventory_transfer/${warehouseId}")
                            },
                            modifier = Modifier.height(36.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = onSurfaceLightMediumContrast,
                                contentColor = onTertiaryContainerLightMediumContrast
                            )
                        ) {
                            Text(" <-> Transfer Products")
                        }
                    }
                }

                // Inventory List
                InventoryList(
                    inventories = inventories,
                    onClick = { inventory ->
                        onNavigate("inventory_details/${inventory.id}")
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                )
            }
        }
    }
}