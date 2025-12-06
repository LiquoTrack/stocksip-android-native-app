package com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.features.authentication.login.presentation.login.LoginViewModel
import com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventory.components.InventoryList
import com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.presentation.account.AccountViewModel
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.shared.ui.components.TopBar
import kotlinx.coroutines.launch

@Composable
fun InventoryView(
    viewModel: InventoryViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    accountViewModel: AccountViewModel = hiltViewModel(),
    warehouseId: String? = null,
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val inventories by viewModel.inventories.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isLoggedOut by loginViewModel.isLoggedOut.collectAsState()
    val userRole by accountViewModel.accountRole.collectAsState()

    val backgroundColor = Color(0xFFF4ECEC)

    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            onLogout()
            loginViewModel.resetLogoutState()
        }
    }

    LaunchedEffect(userRole) {
        if (userRole == null) accountViewModel.loadAccountRoleFromStorage()
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
                },
                userRole = userRole
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = stringResource(R.string.inventory),
                    showBackButton = false,
                    onNavigationClick = { scope.launch { drawerState.open() } }
                )
            },
            containerColor = backgroundColor
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Actions Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.quick_actions),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D1B2E)
                        )


                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Add Inventory Button
                            InventoryActionButton(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.Add,
                                label = stringResource(R.string.add),
                                backgroundColor = Color(0xFF4CAF50),
                                onClick = { onNavigate("inventory_addition/${warehouseId}") }
                            )

                            // Subtract Inventory Button
                            InventoryActionButton(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.Remove,
                                label = stringResource(R.string.subtract),
                                backgroundColor = Color(0xFFF44336),
                                onClick = { onNavigate("inventory_subtrack/${warehouseId}") }
                            )

                            // Transfer Inventory Button
                            InventoryActionButton(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.CompareArrows,
                                label = stringResource(R.string.transfer),
                                backgroundColor = Color(0xFF8B4C5C),
                                onClick = { onNavigate("inventory_transfer/${warehouseId}") }
                            )
                        }
                    }
                }

                // Inventory List Header
                Text(
                    text = stringResource(R.string.products_in_stock),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF8B4C5C)
                )

                // Inventory List
                if (inventories.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.no_inventory_found),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D1B2E)
                            )
                            Text(
                                text = stringResource(R.string.add_products_to_get_started),
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    InventoryList(
                        inventories = inventories,
                        onClick = { inventory ->
                            onNavigate("inventory_detail/${inventory.id}")
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(backgroundColor)
                    )
                }
            }
        }
    }
}

@Composable
fun InventoryActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(72.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = label,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}