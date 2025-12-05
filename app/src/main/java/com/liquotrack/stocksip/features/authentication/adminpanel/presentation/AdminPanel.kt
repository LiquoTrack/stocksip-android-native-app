package com.liquotrack.stocksip.features.authentication.adminpanel.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.features.authentication.login.presentation.login.LoginViewModel
import com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.presentation.account.AccountViewModel
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.shared.ui.components.TopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanel(
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: AdminPanelViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    accountViewModel: AccountViewModel = hiltViewModel()
) {
    val bg = Color(0xFFF5EFED)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val userToDelete by viewModel.userToDelete.collectAsState()
    val userToEdit by viewModel.userToEdit.collectAsState()
    val isLoggedOut by loginViewModel.isLoggedOut.collectAsState()
    val userRole by accountViewModel.accountRole.collectAsState()

    val accountStats = users.firstOrNull()
    val displayedUsers = accountStats?.users ?: emptyList()
    val currentUsersCount = accountStats?.totalUsers ?: displayedUsers.size
    val maxUsersAllowed = accountStats?.maxUsersAllowed
    val isMaxUsersReached = maxUsersAllowed != null && maxUsersAllowed != 0 &&
            currentUsersCount >= maxUsersAllowed
    var showNewUserDialog by remember { mutableStateOf(false) }

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
                currentRoute = "user",
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
                    title = stringResource(id = R.string.admin_panel_title),
                    showBackButton = false,
                    onNavigationClick = { scope.launch { drawerState.open() } }
                )
            },
            containerColor = bg,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showNewUserDialog = true },
                    containerColor = Color(0xFF4A1B2A),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AdminTabButton(
                        text = stringResource(id = R.string.admin_tab_all),
                        isSelected = selectedTab == AdminTab.ALL,
                        onClick = { viewModel.selectTab(AdminTab.ALL) },
                        modifier = Modifier.weight(1f)
                    )
                    AdminTabButton(
                        text = stringResource(id = R.string.admin_tab_admin),
                        isSelected = selectedTab == AdminTab.ADMIN,
                        onClick = { viewModel.selectTab(AdminTab.ADMIN) },
                        modifier = Modifier.weight(1f)
                    )
                    AdminTabButton(
                        text = stringResource(id = R.string.admin_tab_employee),
                        isSelected = selectedTab == AdminTab.EMPLOYEE,
                        onClick = { viewModel.selectTab(AdminTab.EMPLOYEE) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (accountStats != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFEADFE0), RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                stringResource(id = R.string.users_capacity),
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = Color(0xFF4A1B2A)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${currentUsersCount}/${maxUsersAllowed?.toString() ?: "--"}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = Color(0xFF4A1B2A)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = if (isMaxUsersReached)
                                        stringResource(id = R.string.status_max_reached)
                                    else stringResource(id = R.string.status_available),
                                    color = if (isMaxUsersReached) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxSize(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE7DCDC))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFFFF2F2))
                            .padding(12.dp)
                    ) {
                        UsersList(
                            users = displayedUsers,
                            isLoading = isLoading,
                            onEditUser = { viewModel.selectUserForEdit(it) },
                            onDeleteUser = { viewModel.selectUserForDelete(it) }
                        )
                    }
                }
            }
        }
    }

    if (showNewUserDialog) {
        NewUserDialog(
            onDismiss = { showNewUserDialog = false },
            onSave = {
                viewModel.createUser(it)
                showNewUserDialog = false
            }
        )
    }

    userToEdit?.let { user ->
        EditUserDialog(
            user = user,
            onDismiss = { viewModel.clearUserToEdit() },
            onSave = { viewModel.updateUser(it); viewModel.clearUserToEdit() }
        )
    }

    userToDelete?.let { user ->
        DeleteUserDialog(
            userName = user.id,
            onConfirm = { viewModel.deleteUser(user); viewModel.clearUserToDelete() },
            onDismiss = { viewModel.clearUserToDelete() }
        )
    }
}


@Composable
private fun AdminTabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFFF4ECEC) else Color.White,
            contentColor = Color(0xFF4A1B2A)
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFD1C4C4)),
        modifier = modifier.defaultMinSize(minWidth = 108.dp, minHeight = 40.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text, fontSize = 13.sp, maxLines = 1)
    }
}

@Composable
private fun DeleteUserDialog(
    userName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = null,
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringResource(id = R.string.delete_user_confirmation),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color(0xFF4A1B2A)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
            ) {
                Text(stringResource(id = R.string.delete), color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFD1C4C4)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
            ) {
                Text(stringResource(id = R.string.cancel), color = Color(0xFF4A1B2A))
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color(0xFFF4ECEC)
    )
}

enum class AdminTab {
    ALL,
    ADMIN,
    EMPLOYEE,
}
