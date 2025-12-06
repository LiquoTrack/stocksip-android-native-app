package com.liquotrack.stocksip.features.profilemanagement.profile.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddReaction
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.features.authentication.login.presentation.login.LoginViewModel
import com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.presentation.account.AccountViewModel
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.shared.ui.components.TopBar
import kotlinx.coroutines.launch

@Composable
fun Profile(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    loginViewModel: LoginViewModel = hiltViewModel(),
    accountViewModel: AccountViewModel = hiltViewModel()
) {
    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val fullName by viewModel.fullName.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val assignedRole by viewModel.assignedRole.collectAsState()
    val profilePictureUrl by viewModel.profilePictureUrl.collectAsState()
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()
    val isEditMode by viewModel.isEditMode.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val userRole by accountViewModel.accountRole.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isLoggedOut by loginViewModel.isLoggedOut.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            onLogout()
            loginViewModel.resetLogoutState()
        }
    }

    LaunchedEffect(userRole) {
        if (userRole == null) accountViewModel.loadAccountRoleFromStorage()
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    LaunchedEffect(successMessage) {
        successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSuccess()
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { viewModel.updateProfileImage(it) } }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                currentRoute = "profile",
                onNavigate = onNavigate,
                onClose = { scope.launch { drawerState.close() } },
                onLogout = { loginViewModel.logout() },
                userRole = userRole
            )
        },
        gesturesEnabled = !isEditMode
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = stringResource(R.string.profile_title),
                    showBackButton = isEditMode,
                    onNavigationClick = {
                        if (isEditMode && !isSaving) {
                            viewModel.toggleEditMode()
                        } else if (!isEditMode) {
                            scope.launch { drawerState.open() }
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            containerColor = Color(0xFFF4ECEC)
        ) { padding ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF4A1B2A))
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(32.dp))

                    Box(contentAlignment = Alignment.Center) {
                        val imageToShow = if (isEditMode && selectedImageUri != null) selectedImageUri else profilePictureUrl

                        if (imageToShow == null) {
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFB8D4E8)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddReaction,
                                    contentDescription = stringResource(R.string.default_profile_image_description),
                                    tint = Color(0xFF2196F3),
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                        } else {
                            AsyncImage(
                                model = imageToShow,
                                contentDescription = stringResource(R.string.profile_image_description),
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color(0xFF4A1B2A), CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    if (isEditMode) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A1B2A)),
                            shape = RoundedCornerShape(20.dp),
                            enabled = !isSaving
                        ) {
                            Text(
                                text = if (selectedImageUri != null)
                                    stringResource(R.string.change_image)
                                else
                                    stringResource(R.string.select_image),
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    ProfileField(
                        label = stringResource(R.string.first_name_label),
                        value = firstName,
                        onValueChange = viewModel::updateFirstName,
                        isEditMode = isEditMode,
                        enabled = !isSaving
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    ProfileField(
                        label = stringResource(R.string.last_name_label),
                        value = lastName,
                        onValueChange = viewModel::updateLastName,
                        isEditMode = isEditMode,
                        enabled = !isSaving
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    ProfileField(
                        label = stringResource(R.string.phone_number_label),
                        value = phoneNumber,
                        onValueChange = viewModel::updatePhoneNumber,
                        isEditMode = isEditMode,
                        enabled = !isSaving
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    ProfileField(
                        label = stringResource(R.string.assigned_role_label),
                        value = assignedRole,
                        onValueChange = viewModel::updateAssignedRole,
                        isEditMode = isEditMode,
                        enabled = !isSaving
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = {
                            if (isEditMode) viewModel.saveProfile() else viewModel.toggleEditMode()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A1B2A)),
                        shape = RoundedCornerShape(24.dp),
                        enabled = !isSaving
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = if (isEditMode)
                                    stringResource(R.string.save_button)
                                else
                                    stringResource(R.string.edit_profile_button),
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
