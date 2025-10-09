package com.liquotrack.stocksip.features.authentication.login.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.shared.ui.theme.StockSipTheme
import com.liquotrack.stocksip.shared.ui.theme.onSurfaceLight

@Composable
fun Login(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit = {},
    onNavigateToRecovery: () -> Unit = {},
    onLoginSuccess: () -> Unit = {}
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val passwordVisible by viewModel.passwordVisible.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val user by viewModel.user.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Navigate to the next screen when login succeeds
    LaunchedEffect(user) {
        user?.let { onLoginSuccess() }
    }

    // Display errors as snackbars
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Top background section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(0.57f)
                .background(Color(0xFF2B000D))
        )

        // Bottom background section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(0.43f)
                .align(Alignment.BottomCenter)
                .background(Color(0xFFF4ECEC))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            // App logo
            Image(
                painter = painterResource(id = R.drawable.stocksip_logo1),
                contentDescription = "StockSip Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // App name text
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFE53E3E),
                            fontWeight = FontWeight.Bold
                        )
                    ) { append("Stock") }
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    ) { append("Sip") }
                },
                fontSize = 60.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(80.dp))

            // Email input field
            OutlinedTextField(
                value = email,
                onValueChange = viewModel::updateEmail,
                placeholder = { Text("Email", color = Color(0xFF8B7375)) },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Email", tint = Color(0xFF8B7375))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.9f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = onSurfaceLight,
                    unfocusedTextColor = onSurfaceLight
                ),
                shape = RoundedCornerShape(28.dp),
                singleLine = true,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password input field
            OutlinedTextField(
                value = password,
                onValueChange = viewModel::updatePassword,
                placeholder = { Text("Password", color = Color(0xFF8B7375)) },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Password", tint = Color(0xFF8B7375))
                },
                trailingIcon = {
                    IconButton(onClick = viewModel::togglePasswordVisibility) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = Color(0xFF8B7375)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.9f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = onSurfaceLight,
                    unfocusedTextColor = onSurfaceLight
                ),
                shape = RoundedCornerShape(28.dp),
                singleLine = true,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(8.dp))

            // "Forgot Password?" clickable text -> navigates to PasswordRecover screen
            Text(
                text = "Forgot Password?",
                color = Color(0xFFE53E3E),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.clickable(
                    enabled = !isLoading,
                    onClick = onNavigateToRecovery
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sign-in button
            Button(
                onClick = viewModel::login,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A1B2A)
                ),
                shape = RoundedCornerShape(28.dp),
                enabled = !isLoading && email.isNotBlank() && password.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Sign In",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign-up navigation text
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Text(
                    text = "Sign Up",
                    color = Color(0xFFE53E3E),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable(
                        enabled = !isLoading,
                        onClick = onNavigateToRegister
                    )
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // Snackbar for showing error messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = Color(0xFFE53E3E),
                contentColor = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    StockSipTheme {
        Login()
    }
}