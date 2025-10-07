package com.liquotrack.stocksip.features.authentication.register.presentation.register

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liquotrack.stocksip.shared.ui.theme.StockSipTheme

@Composable
fun RegisterAccount() {
    var selectedRole by remember { mutableStateOf("Liquor Store Owner") }
    var businessName by remember { mutableStateOf("") }

    StockSipTheme {
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
                    .verticalScroll(rememberScrollState())
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(120.dp))

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
                    fontSize = 50.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(80.dp))


                // Role Selection
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.LightGray, fontWeight = FontWeight.Bold)) {
                            append("Choose Your Role *")
                        }
                    },
                    color = Color.LightGray,
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { selectedRole = "Liquor Store Owner" },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedRole == "Liquor Store Owner") Color(0xFF4A1B2A) else Color.Transparent
                        ),
                        border = if (selectedRole != "Liquor Store Owner") {
                            androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                        } else null,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "Liquor Store Owner",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = if (selectedRole == "Liquor Store Owner") FontWeight.Medium else FontWeight.Normal
                        )
                    }

                    Button(
                        onClick = { selectedRole = "Supplier" },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedRole == "Supplier") Color(0xFF4A1B2A) else Color.Transparent
                        ),
                        border = if (selectedRole != "Supplier") {
                            androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                        } else null,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "Supplier",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = if (selectedRole == "Supplier") FontWeight.Medium else FontWeight.Normal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Account Info Text
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.LightGray, fontWeight = FontWeight.Bold)) {
                            append("Account Info")
                        }
                    },
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Business Name TextField
                OutlinedTextField(
                    value = businessName,
                    onValueChange = { businessName = it },
                    placeholder = {
                        Text(
                            text = "Business Name",
                            color = Color(0xFF8B7375)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = "Business Icon",
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
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(28.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))



                Spacer(modifier = Modifier.height(80.dp))

                // Sign Up Button
                Button(
                    onClick = { /* Handle sign up */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A1B2A)
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        text = "Sign Up",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterAccountPreview() {
    StockSipTheme {
        RegisterAccount()
    }
}