package com.liquotrack.stocksip.features.authentication.login.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.shared.ui.theme.StockSipTheme
import com.liquotrack.stocksip.shared.ui.theme.onSurfaceLight

@Composable
fun Login() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(0.57f)
                .background(Color(0xFF2B000D))
        )

        // Bottom Background
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

            // Logo Image
            Image(
                painter = painterResource(id = R.drawable.stocksip_logo1),
                contentDescription = "StockSip Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // App Name
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFFE53E3E), fontWeight = FontWeight.Bold)) {
                        append("Stock")
                    }
                    withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                        append("Sip")
                    }
                },
                fontSize = 60.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(80.dp))

            // Email TextField
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(
                        text = "Email",
                        color = Color(0xFF8B7375)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon",
                        tint = Color(0xFF8B7375)
                    )
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
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password TextField
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = {
                    Text(
                        text = "Password",
                        color = Color(0xFF8B7375)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock Icon",
                        tint = Color(0xFF8B7375)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
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
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Forgot Password text
            val forgotPasswordText = buildAnnotatedString {
                pushStringAnnotation(tag = "FORGOT_PASSWORD", annotation = "forgot_password")
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFFE53E3E),
                        fontSize = 14.sp
                    )
                ) {
                    append("Forgot Password?")
                }
                pop()
            }

            ClickableText(
                text = forgotPasswordText,
                onClick = { offset ->
                    forgotPasswordText.getStringAnnotations(
                        tag = "FORGOT_PASSWORD",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        // Handle forgot password click
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                style = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.End)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = { /* Handle login */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A1B2A)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Sign In",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Register text
            val annotatedText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append("Don't have an account? ")
                }
                pushStringAnnotation(tag = "REGISTER", annotation = "register")
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFFE53E3E),
                        fontWeight = FontWeight.Medium
                    )
                ) {
                    append("Sign Up")
                }
                pop()
            }

            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations(
                        tag = "REGISTER",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        // Handle register click
                    }
                }
            )

            Spacer(modifier = Modifier.height(40.dp))
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