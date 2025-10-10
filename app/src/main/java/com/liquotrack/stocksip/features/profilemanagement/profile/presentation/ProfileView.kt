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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.shared.ui.components.TopBar
import com.liquotrack.stocksip.shared.ui.theme.StockSipTheme
import kotlinx.coroutines.launch

@Composable
fun Profile(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit = {}
) {
    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val contactNumber by viewModel.contactNumber.collectAsState()
    val profileImageUrl by viewModel.profileImageUrl.collectAsState()
    val isEditMode by viewModel.isEditMode.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.uploadProfileImage(it) }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                currentRoute = "profile",
                onNavigate = onNavigate,
                onClose = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        },
        gesturesEnabled = !isEditMode
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = "Profile",
                    showBackButton = isEditMode,
                    onNavigationClick = {
                        if (isEditMode && !isSaving) {
                            viewModel.toggleEditMode()
                        } else if (!isEditMode) {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    }
                )
            },
            containerColor = Color(0xFFF4ECEC)
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Profile Image
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    if (profileImageUrl.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFB8D4E8)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Default Profile",
                                tint = Color(0xFF4A1B2A),
                                modifier = Modifier.size(60.dp)
                            )
                        }
                    } else {
                        AsyncImage(
                            model = profileImageUrl,
                            contentDescription = "Profile Image",
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
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4A1B2A)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        enabled = !isSaving
                    ) {
                        Text(
                            text = "Select Image",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Name Field
                ProfileField(
                    label = "Name",
                    value = name,
                    onValueChange = viewModel::updateName,
                    isEditMode = isEditMode,
                    enabled = !isSaving
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Email Field
                ProfileField(
                    label = "Email",
                    value = email,
                    onValueChange = viewModel::updateEmail,
                    isEditMode = isEditMode,
                    enabled = !isSaving
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Contact Number Field
                ProfileField(
                    label = "Contact Number",
                    value = contactNumber,
                    onValueChange = viewModel::updateContactNumber,
                    isEditMode = isEditMode,
                    enabled = !isSaving
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Action Button
                Button(
                    onClick = {
                        if (isEditMode) {
                            viewModel.saveProfile()
                        } else {
                            viewModel.toggleEditMode()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A1B2A)
                    ),
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
                            text = if (isEditMode) "Save" else "Edit Profile",
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

@Preview
@Composable
fun ProfilePreview() {
    StockSipTheme {
        Profile()
    }
}