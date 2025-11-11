package com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.features.authentication.login.presentation.login.LoginViewModel
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse.components.WarehouseList
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.shared.ui.components.TopBar
import com.liquotrack.stocksip.shared.ui.theme.onSurfaceLightMediumContrast
import com.liquotrack.stocksip.shared.ui.theme.onTertiaryContainerLightMediumContrast
import kotlinx.coroutines.launch


@Composable
fun WarehouseView(
    viewModel: WarehouseViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val warehouses by viewModel.warehouses.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isLoggedOut by loginViewModel.isLoggedOut.collectAsState()

    val backgroundColor  = Color(0xFFF4ECEC)

    val isMaxReached by viewModel.isMaxReached.collectAsState()

    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            onLogout()
            loginViewModel.resetLogoutState()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getAllWarehousesByAccountId()
    }

    LaunchedEffect(warehouses) {
        viewModel.validateMaxWarehouses()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                currentRoute = "warehouse",
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
                    title = stringResource(R.string.label_warehouses),
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
                            Column {
                                Row {
                                    Text(
                                        stringResource(R.string.label_current_warehouses),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = onSurfaceLightMediumContrast,
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "${warehouses?.total}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = onSurfaceLightMediumContrast
                                    )
                                }

                                Row {
                                    Text(
                                        stringResource(R.string.label_max_warehouses),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = onSurfaceLightMediumContrast
                                    )
                                    Text(
                                        "${warehouses?.maxWarehousesAllowed}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = onSurfaceLightMediumContrast
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = {
                                onNavigate("warehouse_create_edit/new")
                            },
                            enabled = !isMaxReached,
                            modifier = Modifier.height(36.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = onSurfaceLightMediumContrast,
                                contentColor = onTertiaryContainerLightMediumContrast
                            )
                        ) {
                            Text(" + ${stringResource(R.string.label_new_warehouse)}")
                        }
                    }
                }

                WarehouseList(
                    warehouse = warehouses?.warehouses ?: emptyList(),
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