package com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.presentation.PurchaseOrdersViewModel
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.remote.models.AddressDto
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.remote.models.AddressRequestDto
import com.liquotrack.stocksip.features.procurementordering.purchaseorders.presentation.cart.CartViewModel
import com.liquotrack.stocksip.shared.ui.components.TopBarWithBack

@Composable
fun AddressListScreen(
    addressViewModel: AddressViewModel = hiltViewModel(),
    purchaseOrdersViewModel: PurchaseOrdersViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onOrderCreated: (String) -> Unit
) {
    val addresses by addressViewModel.addresses.collectAsState()
    val selectedAddressIndex by addressViewModel.selectedAddressIndex.collectAsState()
    val showDialog by addressViewModel.showDialog.collectAsState()
    val isLoading by addressViewModel.isLoading.collectAsState()
    val errorMessage by addressViewModel.errorMessage.collectAsState()

    val isOrderLoading by purchaseOrdersViewModel.isLoading.collectAsState()
    val isBusy = isLoading || isOrderLoading

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            addressViewModel.clearError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4ECEC))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBarWithBack(
                title = "Select Address",
                onBackClick = onBackClick
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                when {
                    isLoading && addresses.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF8B4C5C))
                        }
                    }

                    addresses.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    "No addresses yet",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2D1B2E)
                                )
                                Text(
                                    "Add your first address",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            itemsIndexed(
                                items = addresses,
                                key = { index, _ -> index }
                            ) { index, address ->
                                AddressCard(
                                    address = address,
                                    addressIndex = index,
                                    isSelected = index == selectedAddressIndex,
                                    onSelect = { addressViewModel.selectAddress(index) }
                                )
                            }
                            item { Spacer(modifier = Modifier.height(16.dp)) }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val address = selectedAddressIndex?.let { addresses[it] }
                        val catalogId = cartViewModel.catalogIdBuyFrom
                        val cartItems = cartViewModel.cartItems.value

                        Log.d("ADDRESS_SCREEN", "Button clicked. Selected index=$selectedAddressIndex, catalogId=$catalogId, cartItems=$cartItems")

                        if (address != null && !catalogId.isNullOrEmpty() && cartItems.isNotEmpty()) {
                            purchaseOrdersViewModel.createFullOrder(
                                catalogIdBuyFrom = catalogId,
                                addressIndex = selectedAddressIndex
                            )
                            cartViewModel.clearCart()
                            onOrderCreated(catalogId)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(8.dp, RoundedCornerShape(28.dp), spotColor = Color(0xFF8B4C5C)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2D1B2E)
                    ),
                    shape = RoundedCornerShape(28.dp),
                    enabled = !isBusy && selectedAddressIndex != null
                ) {
                    if (isOrderLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Create Order",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { addressViewModel.showAddDialog() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(bottom = 80.dp),
            containerColor = Color(0xFF8B4C5C),
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add address",
                tint = Color.White
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    if (showDialog) {
        AddAddressDialog(
            onDismiss = { addressViewModel.hideAddDialog() },
            onSave = { address -> addressViewModel.addAddress(address) },
            isLoading = isBusy
        )
    }
}


@Composable
fun AddressCard(
    address: AddressDto,
    addressIndex: Int,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onSelect() },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF8B4C5C),
                    uncheckedColor = Color(0xFF8B4C5C)
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = address.street ?: "",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D1B2E),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "#${addressIndex + 1}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF8B4C5C)
                    )
                }
                Text(
                    text = "${address.city ?: ""}, ${address.state ?: ""}",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
                if (!address.zipCode.isNullOrEmpty()) {
                    Text(
                        text = "${address.country ?: ""} - ${address.zipCode}",
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
        }
    }
}

@Composable
fun AddAddressDialog(
    onDismiss: () -> Unit,
    onSave: (AddressRequestDto) -> Unit,
    isLoading: Boolean
) {
    var street by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { if (!isLoading) onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "New Address",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D1B2E)
                )

                OutlinedTextField(
                    value = street,
                    onValueChange = { street = it },
                    label = { Text("Street") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF8B4C5C),
                        focusedLabelColor = Color(0xFF8B4C5C),
                        cursorColor = Color(0xFF8B4C5C)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF8B4C5C),
                        focusedLabelColor = Color(0xFF8B4C5C),
                        cursorColor = Color(0xFF8B4C5C)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = state,
                    onValueChange = { state = it },
                    label = { Text("State") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF8B4C5C),
                        focusedLabelColor = Color(0xFF8B4C5C),
                        cursorColor = Color(0xFF8B4C5C)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = country,
                    onValueChange = { country = it },
                    label = { Text("Country") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF8B4C5C),
                        focusedLabelColor = Color(0xFF8B4C5C),
                        cursorColor = Color(0xFF8B4C5C)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = zipCode,
                    onValueChange = { zipCode = it },
                    label = { Text("Zip Code") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF8B4C5C),
                        focusedLabelColor = Color(0xFF8B4C5C),
                        cursorColor = Color(0xFF8B4C5C)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { if (!isLoading) onDismiss() },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF2D1B2E)
                        )
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            if (street.isNotBlank() && city.isNotBlank()) {
                                onSave(
                                    AddressRequestDto(
                                        street = street,
                                        city = city,
                                        state = state,
                                        country = country,
                                        zipCode = zipCode
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .shadow(8.dp, RoundedCornerShape(28.dp), spotColor = Color(0xFF8B4C5C)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2D1B2E)
                        ),
                        shape = RoundedCornerShape(28.dp),
                        enabled = !isLoading && street.isNotBlank() && city.isNotBlank()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Save",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}