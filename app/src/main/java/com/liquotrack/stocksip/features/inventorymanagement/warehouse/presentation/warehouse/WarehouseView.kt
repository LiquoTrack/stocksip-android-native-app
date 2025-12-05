package com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseResponse
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse.components.WarehouseList
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.shared.ui.components.TopBar
import com.liquotrack.stocksip.shared.ui.theme.onSurfaceLightMediumContrast
import com.liquotrack.stocksip.shared.ui.theme.onTertiaryContainerLightMediumContrast
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
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

    val backgroundColor = Color(0xFFF4ECEC)
    val isMaxReached by viewModel.isMaxReached.collectAsState()

    var selectedWarehouseForSheet by remember { mutableStateOf<WarehouseResponse?>(null) }
    val showSheet = selectedWarehouseForSheet != null

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
                onClose = { scope.launch { drawerState.close() } },
                onLogout = { loginViewModel.logout() }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = stringResource(R.string.label_warehouses),
                    showBackButton = false,
                    onNavigationClick = { scope.launch { drawerState.open() } }
                )
            },
            containerColor = backgroundColor,
            floatingActionButton = {
                if (!isMaxReached) {
                    androidx.compose.material3.FloatingActionButton(
                        onClick = { onNavigate("warehouse_create_edit/new") },
                        containerColor = Color(0xFF4A1B2A),
                        contentColor = onTertiaryContainerLightMediumContrast
                    ) { Text("+") }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(
                            color = onTertiaryContainerLightMediumContrast,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${stringResource(R.string.label_current)}:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = onSurfaceLightMediumContrast
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "${warehouses?.total ?: "0"}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = onSurfaceLightMediumContrast
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${stringResource(R.string.label_max)}:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = onSurfaceLightMediumContrast
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "${warehouses?.maxWarehousesAllowed ?: "0"}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = onSurfaceLightMediumContrast
                            )
                        }
                    }
                }

                WarehouseList(
                    warehouse = warehouses?.warehouses ?: emptyList(),
                    onClick = { warehouse -> onNavigate("warehouse_inventory/${warehouse.id}") },
                    onEditClick = { warehouse -> onNavigate("warehouse_create_edit/${warehouse.id}") },
                    onLongPress = { warehouse -> selectedWarehouseForSheet = warehouse },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                )
            }

            if (showSheet) {
                ModalBottomSheet(
                    onDismissRequest = { selectedWarehouseForSheet = null }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        Text(
                            text = stringResource(R.string.label_edit),
                            fontSize = 16.sp,
                            color = Color(0xFF4A1B2A),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedWarehouseForSheet?.let { onNavigate("warehouse_create_edit/${it.id}") }
                                    selectedWarehouseForSheet = null
                                }
                                .padding(vertical = 12.dp)
                        )

                        Text(
                            text = stringResource(R.string.label_delete),
                            fontSize = 16.sp,
                            color = Color.Red,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedWarehouseForSheet?.let { viewModel.deleteWarehouseById(it.id) { } }
                                    selectedWarehouseForSheet = null
                                }
                                .padding(vertical = 12.dp)
                        )
                    }
                }
            }
        }
    }
}
