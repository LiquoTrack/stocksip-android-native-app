package com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.storage

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
import androidx.compose.material3.MaterialTheme
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
import com.liquotrack.stocksip.features.authentication.login.presentation.login.LoginViewModel
import com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.storage.components.ProductList
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.shared.ui.components.TopBar
import com.liquotrack.stocksip.shared.ui.theme.onSurfaceLightMediumContrast
import com.liquotrack.stocksip.shared.ui.theme.onTertiaryContainerLightMediumContrast
import kotlinx.coroutines.launch

@Composable
fun StorageView(
    viewModel: StorageViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsState()
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
        viewModel.getAllProductsByAccountId()
    }

    LaunchedEffect(products) {
        viewModel.validateMaxProducts()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                currentRoute = "products_storage",
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
                    title = "Storage",
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
                                        "Current: ",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = onSurfaceLightMediumContrast,
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "${products?.total}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = onSurfaceLightMediumContrast
                                    )
                                }

                                Row {
                                    Text(
                                        "Max. Allowed: ",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = onSurfaceLightMediumContrast
                                    )
                                    Text(
                                        "${products?.maxProductsAllowed}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = onSurfaceLightMediumContrast
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = {
                                onNavigate("product_create_edit/new")
                            },
                            enabled = !isMaxReached,
                            modifier = Modifier.height(36.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = onSurfaceLightMediumContrast,
                                contentColor = onTertiaryContainerLightMediumContrast
                            )
                        ) {
                            Text(" + New Product")
                        }
                    }
                }

                ProductList(
                    products = products?.products ?: emptyList(),
                    onClick = { product ->
                        onNavigate("product_details/${product.id}")
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                )
            }
        }
    }
}