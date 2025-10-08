package com.liquotrack.stocksip.features.adminpanel.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanel(
    username: String = "Admin User",
    onNavigate: (String) -> Unit = {},
    viewModel: AdminPanelViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    var showNewUserDialog by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                username = username,
                currentRoute = "admin",
                onNavigate = onNavigate,
                onClose = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Administrative Panel",
                            color = Color(0xFF4A1B2A),
                            fontWeight = FontWeight.Medium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
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
                // Tabs: Users / Roles
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AdminTabButton(
                        text = "Users",
                        isSelected = selectedTab == AdminTab.USERS,
                        onClick = { viewModel.selectTab(AdminTab.USERS) }
                    )

                    AdminTabButton(
                        text = "Roles",
                        isSelected = selectedTab == AdminTab.ROLES,
                        onClick = { viewModel.selectTab(AdminTab.ROLES) }
                    )
                }

                // New User Button
                Button(
                    onClick = { showNewUserDialog = true },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(55.dp)
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A1B2A)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 4.dp)
                    )
                    Text("New", color = Color.White, fontSize = 14.sp)
                }

                // Content based on selected tab
                when (selectedTab) {
                    AdminTab.USERS -> {
                        UsersList(
                            users = users,
                            isLoading = isLoading,
                            onEditUser = { user ->
                                viewModel.selectUserForEdit(user)
                            },
                            onDeleteUser = { user ->
                                viewModel.selectUserForDelete(user)
                            }
                        )
                    }

                    AdminTab.ROLES -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Roles Management - Coming Soon")
                        }
                    }
                }
            }
        }
    }

    // New User Dialog
    if (showNewUserDialog) {
        NewUserDialog(
            onDismiss = { showNewUserDialog = false },
            onSave = { newUser ->
                viewModel.createUser(newUser)
                showNewUserDialog = false
            }
        )
    }

    // Edit User Dialog
    viewModel.userToEdit.value?.let { user ->
        EditUserDialog(
            user = user,
            onDismiss = { viewModel.clearUserToEdit() },
            onSave = { updatedUser ->
                viewModel.updateUser(updatedUser)
                viewModel.clearUserToEdit()
            }
        )
    }

    // Delete User Dialog
    viewModel.userToDelete.value?.let { user ->
        DeleteUserDialog(
            userName = user.username,
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
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFFF4ECEC) else Color.White,
            contentColor = Color(0xFF4A1B2A)
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFD1C4C4)),
        modifier = Modifier
            .width(100.dp)
            .height(36.dp)
    ) {
        Text(text, fontSize = 14.sp)
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
                    "Are you sure you\nwant to delete this\nuser?",
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text("Delete", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFD1C4C4)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text("Cancel", color = Color(0xFF4A1B2A))
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color(0xFFF4ECEC)
    )
}

enum class AdminTab {
    USERS,
    ROLES
}