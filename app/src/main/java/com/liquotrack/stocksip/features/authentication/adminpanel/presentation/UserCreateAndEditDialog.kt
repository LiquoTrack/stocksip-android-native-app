package com.liquotrack.stocksip.features.authentication.adminpanel.presentation

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liquotrack.stocksip.features.authentication.adminpanel.domain.domain.AccountUsers
import com.liquotrack.stocksip.features.authentication.adminpanel.domain.domain.SubUser
import com.liquotrack.stocksip.R

@Composable
fun NewUserDialog(
    onDismiss: () -> Unit,
    onSave: (SubUser) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Admin") }
    var phone by remember { mutableStateOf("") }
    var showRoleDropdown by remember { mutableStateOf(false) }

    val roles = listOf(
        "Admin" to stringResource(id = R.string.admin_role_admin),
        "Manager" to stringResource(id = R.string.admin_role_manager),
        "Cashier" to stringResource(id = R.string.admin_role_cashier),
        "Worker" to stringResource(id = R.string.admin_role_worker)
    )
    val selectedRoleLabel = roles.firstOrNull { it.first.equals(selectedRole, ignoreCase = true) }?.second ?: selectedRole

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(id = R.string.new_user_title), color = Color(0xFF4A1B2A), fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(stringResource(id = R.string.user_info_section), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE53E3E))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text(stringResource(id = R.string.name_placeholder)) },
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
                    placeholder = { Text(stringResource(id = R.string.email_placeholder)) },
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
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = { Text(stringResource(id = R.string.phone_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFD1C4C4),
                        unfocusedBorderColor = Color(0xFFD1C4C4)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Text(stringResource(id = R.string.role_assigned), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE53E3E))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedRoleLabel,
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
                                contentDescription = stringResource(id = R.string.dropdown_content_description),
                                tint = Color(0xFF4A1B2A)
                            )
                        }
                    )

                    DropdownMenu(
                        expanded = showRoleDropdown,
                        onDismissRequest = { showRoleDropdown = false }
                    ) {
                        roles.forEach { (value, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedRole = value
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
                    val newUser = SubUser(
                        id = "",
                        email = email.trim(),
                        userRole = selectedRole,
                        profileId = "",
                        fullName = name.trim(),
                        phoneNumber = phone.trim(),
                        profilePictureUrl = "",
                        profileRole = selectedRole
                    )
                    onSave(newUser)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A1B2A)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(stringResource(id = R.string.save), color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFD1C4C4)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(stringResource(id = R.string.cancel), color = Color(0xFF4A1B2A))
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color(0xFFF4ECEC)
    )
}

@Composable
fun EditUserDialog(
    user: SubUser,
    onDismiss: () -> Unit,
    onSave: (SubUser) -> Unit
) {
    var name by remember { mutableStateOf(user.fullName) }
    var email by remember { mutableStateOf(user.email) }
    var showRoleDropdown by remember { mutableStateOf(false) }

    val roles = listOf(
        "Admin" to stringResource(id = R.string.admin_role_admin),
        "Employee" to stringResource(id = R.string.admin_role_employee)
    )
    var selectedRole by remember { mutableStateOf(user.profileRole.ifBlank { user.userRole }.ifBlank { roles.first().first }) }
    val selectedRoleLabel = roles.firstOrNull { it.first.equals(selectedRole, ignoreCase = true) }?.second ?: selectedRole

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
                        contentDescription = stringResource(id = com.liquotrack.stocksip.R.string.print),
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
                Text(stringResource(id = com.liquotrack.stocksip.R.string.user_info_section), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE53E3E))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text(stringResource(id = com.liquotrack.stocksip.R.string.name_placeholder)) },
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
                    placeholder = { Text(stringResource(id = com.liquotrack.stocksip.R.string.email_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFD1C4C4),
                        unfocusedBorderColor = Color(0xFFD1C4C4)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Text(stringResource(id = com.liquotrack.stocksip.R.string.role_assigned), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE53E3E))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedRoleLabel,
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
                                contentDescription = stringResource(id = com.liquotrack.stocksip.R.string.dropdown_content_description),
                                tint = Color(0xFF4A1B2A)
                            )
                        }
                    )

                    DropdownMenu(
                        expanded = showRoleDropdown,
                        onDismissRequest = { showRoleDropdown = false }
                    ) {
                        roles.forEach { (value, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedRole = value
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
                    val updatedUser = user.copy(
                        fullName = name.trim(),
                        email = email.trim(),
                        profileRole = selectedRole,
                        userRole = selectedRole
                    )
                    onSave(updatedUser)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A1B2A)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(stringResource(id = com.liquotrack.stocksip.R.string.save), color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFD1C4C4)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(stringResource(id = com.liquotrack.stocksip.R.string.cancel), color = Color(0xFF4A1B2A))
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color(0xFFF4ECEC)
    )
}