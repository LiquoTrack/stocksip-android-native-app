package com.liquotrack.stocksip.features.adminpanel.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liquotrack.stocksip.shared.domain.model.User

@Composable
fun NewUserDialog(
    onDismiss: () -> Unit,
    onSave: (User) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Admin") }
    var showRoleDropdown by remember { mutableStateOf(false) }

    val roles = listOf("Admin", "Manager", "Cashier", "Worker")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("New User", color = Color(0xFF4A1B2A), fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("User Info", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE53E3E))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFD1C4C4),
                        unfocusedBorderColor = Color(0xFFD1C4C4)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFD1C4C4),
                        unfocusedBorderColor = Color(0xFFD1C4C4)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Text("Role assigned", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE53E3E))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedRole,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showRoleDropdown = true },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color(0xFFD1C4C4),
                            unfocusedBorderColor = Color(0xFFD1C4C4)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                tint = Color(0xFF4A1B2A)
                            )
                        }
                    )

                    DropdownMenu(
                        expanded = showRoleDropdown,
                        onDismissRequest = { showRoleDropdown = false }
                    ) {
                        roles.forEach { role ->
                            DropdownMenuItem(
                                text = { Text(role) },
                                onClick = {
                                    selectedRole = role
                                    showRoleDropdown = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        User(
                            email = email,
                            username = name,
                            userRole = selectedRole,
                            accountId = ""
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A1B2A)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Save", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFD1C4C4)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Cancel", color = Color(0xFF4A1B2A))
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color(0xFFF4ECEC)
    )
}

@Composable
fun EditUserDialog(
    user: User,
    onDismiss: () -> Unit,
    onSave: (User) -> Unit
) {
    var name by remember { mutableStateOf(user.username) }
    var email by remember { mutableStateOf(user.email) }
    var selectedRole by remember { mutableStateOf(user.userRole) }
    var showRoleDropdown by remember { mutableStateOf(false) }

    val roles = listOf("Admin", "Manager", "Cashier", "Worker")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(name, color = Color(0xFF4A1B2A), fontWeight = FontWeight.Bold)
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Print,
                        contentDescription = "Print",
                        tint = Color.Gray
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("User Info", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE53E3E))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFD1C4C4),
                        unfocusedBorderColor = Color(0xFFD1C4C4)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFD1C4C4),
                        unfocusedBorderColor = Color(0xFFD1C4C4)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Text("Role assigned", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE53E3E))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedRole,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showRoleDropdown = true },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color(0xFFD1C4C4),
                            unfocusedBorderColor = Color(0xFFD1C4C4)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                tint = Color(0xFF4A1B2A)
                            )
                        }
                    )

                    DropdownMenu(
                        expanded = showRoleDropdown,
                        onDismissRequest = { showRoleDropdown = false }
                    ) {
                        roles.forEach { role ->
                            DropdownMenuItem(
                                text = { Text(role) },
                                onClick = {
                                    selectedRole = role
                                    showRoleDropdown = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        user.copy(
                            email = email,
                            username = name,
                            userRole = selectedRole
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A1B2A)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Save", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFD1C4C4)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Cancel", color = Color(0xFF4A1B2A))
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color(0xFFF4ECEC)
    )
}