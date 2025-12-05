package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation.storeownercatalogs.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.features.authentication.login.presentation.login.LoginViewModel
import com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.presentation.account.AccountViewModel
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.shared.ui.components.TopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierSearchScreen(
    onNavigate: (String) -> Unit = {},
    onMenuClick: () -> Unit,
    onSupplierSelected: (String) -> Unit, // accountId
    onLogout: () -> Unit = {},
    loginViewModel: LoginViewModel = hiltViewModel(),
    accountViewModel: AccountViewModel = hiltViewModel(),
    viewModel: SupplierSearchViewModel = hiltViewModel()
) {
    var query by remember { mutableStateOf("") }
    val bg = Color(0xFFF4ECEC)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isLoggedOut by loginViewModel.isLoggedOut.collectAsState()
    val userRole by accountViewModel.accountRole.collectAsState()

    // ViewModel states
    val suppliers by viewModel.suppliers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSuppliers()
    }

    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            onLogout()
            loginViewModel.resetLogoutState()
        }
    }

    LaunchedEffect(userRole) {
        if (userRole == null) accountViewModel.loadAccountRoleFromStorage()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                currentRoute = "supplier_search",
                onNavigate = onNavigate,
                onClose = { scope.launch { drawerState.close() } },
                onLogout = { loginViewModel.logout() },
                userRole = userRole
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = stringResource(id = R.string.suppliers_title),
                    showBackButton = false,
                    onNavigationClick = { scope.launch { drawerState.open() } }
                )
            },
            containerColor = bg
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Search bar
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text(stringResource(id = R.string.search_supplier_hint), color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = stringResource(id = R.string.search_icon_desc),
                            tint = Color.Gray
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                when {
                    isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF8B4C5C))
                        }
                    }

                    error != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(stringResource(id = R.string.error_loading_suppliers), color = Color.Red)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(error ?: "", color = Color.Gray, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = { viewModel.loadSuppliers() }) {
                                    Text(stringResource(id = R.string.retry_button))
                                }
                            }
                        }
                    }

                    query.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                stringResource(id = R.string.start_shopping_message),
                                fontSize = 20.sp,
                                color = Color(0xFF8B4C5C),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    else -> {
                        val filteredSuppliers = suppliers.filter {
                            it.account.business.businessName.contains(query, ignoreCase = true) ||
                                    it.account.business.businessEmail.contains(query, ignoreCase = true)
                        }

                        if (filteredSuppliers.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(stringResource(id = R.string.no_suppliers_found), color = Color.Gray)
                            }
                        } else {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(filteredSuppliers.size) { index ->
                                    val supplier = filteredSuppliers[index]
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                onSupplierSelected(supplier.account.id)
                                            },
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                supplier.account.business.businessName,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                stringResource(
                                                    id = R.string.catalogs_available_label,
                                                    supplier.catalogs.size
                                                ),
                                                color = Color.Gray,
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                supplier.account.business.businessEmail,
                                                color = Color.Gray,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
