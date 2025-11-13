package com.liquotrack.stocksip.features.inventorymanagement.careguides.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.core.navigation.Route
import com.liquotrack.stocksip.features.authentication.login.presentation.login.LoginViewModel
import com.liquotrack.stocksip.features.inventorymanagement.careguides.domain.CareGuide
import com.liquotrack.stocksip.features.inventorymanagement.careguides.domain.CareGuideViewModel
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.shared.ui.components.TopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareGuides(
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: CareGuideViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val search = remember { mutableStateOf("") }
    val careGuides = viewModel.careGuides.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedGuide by remember { mutableStateOf<CareGuide?>(null) }
    val showDialog = selectedGuide != null
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
                currentRoute = "care_guide",
                onNavigate = onNavigate,
                onClose = { scope.launch { drawerState.close() } },
                onLogout = { loginViewModel.logout() }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = stringResource(R.string.care_guides_title),
                    showBackButton = false,
                    onNavigationClick = { scope.launch { drawerState.open() } }
                )
            },
            containerColor = Color(0xFFF4ECEC)
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = search.value,
                        onValueChange = { search.value = it },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        },
                        placeholder = {
                            Text(stringResource(R.string.placeholder_search), color = Color.Gray)
                        },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = { onNavigate(Route.CareGuideCreate.route) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53E3E),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.new_button))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(careGuides.value.size) { index ->
                        val careGuide = careGuides.value[index]
                        CareGuideCard(
                            careGuide = careGuide,
                            onClick = {},
                            onSeeGuide = { selectedGuide = it },
                            onEdit = { onNavigate(Route.CareGuideEdit.buildRoute(it.careGuideId)) }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        CareGuideDetailDialog(
            careGuide = selectedGuide!!,
            onDismiss = { selectedGuide = null }
        )
    }
}

@Composable
private fun CareGuideDetailDialog(
    careGuide: CareGuide,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (careGuide.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(careGuide.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = careGuide.productName,
                        modifier = Modifier
                            .height(140.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .height(140.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFE9D9CA)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalDrink,
                            contentDescription = careGuide.productName,
                            tint = Color(0xFF8A3040)
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    DetailRow(title = stringResource(R.string.product_name_label), value = careGuide.productName)
                    DetailRow(
                        title = stringResource(R.string.type),
                        value = careGuide.title.ifBlank { stringResource(R.string.not_available) }
                    )
                    DetailRow(title = stringResource(R.string.comments), value = careGuide.summary)
                    DetailRow(
                        title = stringResource(R.string.min_temp),
                        value = "${careGuide.recommendedMinTemperature}° C"
                    )
                    DetailRow(
                        title = stringResource(R.string.max_temp),
                        value = "${careGuide.recommendedMaxTemperature}° C"
                    )
                }

                TextButton(onClick = onDismiss) {
                    Text(
                        text = stringResource(R.string.close),
                        color = Color(0xFF8A3040),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(title: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, fontWeight = FontWeight.SemiBold, color = Color(0xFF3B2B2B))
        Text(value, color = Color(0xFF737373))
    }
}
