package com.liquotrack.stocksip.features.authentication.adminpanel.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.features.authentication.login.presentation.login.LoginViewModel
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanel(
    onNavigate: (String) -> Unit = {},
    viewModel: AdminPanelViewModel = hiltViewModel(),
    onLogout: () -> Unit = {},
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val userToDelete by viewModel.userToDelete.collectAsState()
    val userToEdit by viewModel.userToEdit.collectAsState()
    val accountStats = users.firstOrNull()
    val allUsers = accountStats?.users ?: emptyList()
    val filteredUsers = when (selectedTab) {
        AdminTab.ALL -> allUsers
        AdminTab.ADMIN -> allUsers.filter {
            it.userRole.equals("Admin", ignoreCase = true) ||
            it.profileRole.equals("Admin", ignoreCase = true)
        }
        AdminTab.EMPLOYEE -> allUsers.filter {
            it.userRole.equals("Employee", ignoreCase = true) ||
            it.profileRole.equals("Seller", ignoreCase = true) ||
            it.profileRole.equals("Buyer", ignoreCase = true) ||
            it.profileRole.equals("WarehouseWorker", ignoreCase = true)
        }
    }
    val currentUsersCount = accountStats?.totalUsers ?: allUsers.size
    val maxUsersAllowed = accountStats?.maxUsersAllowed
    val isMaxUsersReached = maxUsersAllowed != null && maxUsersAllowed != 0 &&
        currentUsersCount >= maxUsersAllowed
    var showNewUserDialog by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isLoggedOut by loginViewModel.isLoggedOut.collectAsState()

    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            onLogout()
            loginViewModel.resetLogoutState()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                currentRoute = "user",
                onNavigate = onNavigate,
                onClose = { scope.launch { drawerState.close() } },
                onLogout = { loginViewModel.logout() }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.admin_panel_title),
                            color = Color(0xFF4A1B2A),
                            fontWeight = FontWeight.Medium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(id = R.string.menu_content_description),
                                tint = Color(0xFF4A1B2A)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFF4ECEC)
                    )
                )
            },
            containerColor = Color(0xFFF4ECEC)
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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

                    Button(
                        onClick = { showNewUserDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A1B2A)),
                        shape = RoundedCornerShape(24.dp),
                        enabled = !isMaxUsersReached,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(stringResource(id = R.string.admin_new_user), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                if (accountStats != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFEADFE0), RoundedCornerShape(16.dp))
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                            .padding(bottom = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.users_capacity),
                                    color = Color(0xFF4A1B2A),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = stringResource(id = R.string.users_capacity_value, currentUsersCount, maxUsersAllowed?.toString() ?: "--"),
                                    color = Color(0xFF4A1B2A),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }

                            Text(
                                text = if (isMaxUsersReached) stringResource(id = R.string.status_max_reached) else stringResource(id = R.string.status_available),
                                color = if (isMaxUsersReached) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                ) {
                    when (selectedTab) {
                        AdminTab.ALL -> {
                            UsersList(
                                users = filteredUsers,
                                isLoading = isLoading,
                                onEditUser = { user -> viewModel.selectUserForEdit(user) },
                                onDeleteUser = { user -> viewModel.selectUserForDelete(user) }
                            )
                        }
                        AdminTab.ADMIN -> {
                            UsersList(
                                users = filteredUsers,
                                isLoading = isLoading,
                                onEditUser = { user -> viewModel.selectUserForEdit(user) },
                                onDeleteUser = { user -> viewModel.selectUserForDelete(user) }
                            )
                        }
                        AdminTab.EMPLOYEE -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                UsersList(
                                    users = filteredUsers,
                                    isLoading = isLoading,
                                    onEditUser = { user -> viewModel.selectUserForEdit(user) },
                                    onDeleteUser = { user -> viewModel.selectUserForDelete(user) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showNewUserDialog) {
        NewUserDialog(
            onDismiss = { showNewUserDialog = false },
            onSave = { newUser ->
                viewModel.createUser(newUser)
                // Asegurar visibilidad inmediata del nuevo usuario
                if (selectedTab != AdminTab.ALL) {
                    viewModel.selectTab(AdminTab.ALL)
                }
                showNewUserDialog = false
            }
        )
    }

    userToEdit?.let { user ->
        EditUserDialog(
            user = user,
            onDismiss = { viewModel.clearUserToEdit() },
            onSave = { updatedUser ->
                viewModel.updateUser(user)
                viewModel.clearUserToEdit()
            }
        )
    }

    userToDelete?.let { user ->
        DeleteUserDialog(
            userName = user.id,
            onConfirm = {
                viewModel.deleteUser(user)
                viewModel.clearUserToDelete()
            },
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