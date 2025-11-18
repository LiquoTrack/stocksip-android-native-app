package com.liquotrack.stocksip.features.home.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.features.authentication.login.presentation.login.LoginViewModel
import com.liquotrack.stocksip.features.home.domain.model.ShortcutItem
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.shared.ui.components.TopBar
import kotlinx.coroutines.launch

@Composable
fun HomeView(
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isLoggedOut by loginViewModel.isLoggedOut.collectAsState()

    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            onLogout()
            loginViewModel.resetLogoutState()
        }
    }

    val shortcuts = listOf(
        ShortcutItem(
            title = stringResource(R.string.shortcut_new_product_title),
            description = "",
            buttonLabel = stringResource(R.string.shortcut_new_product_button),
            iconRes = R.drawable.vino1
        ),
        ShortcutItem(
            title = stringResource(R.string.shortcut_new_order_title),
            description = "",
            buttonLabel = stringResource(R.string.shortcut_new_order_button),
            iconRes = R.drawable.nota1
        ),
        ShortcutItem(
            title = stringResource(R.string.shortcut_new_user_title),
            description = "",
            buttonLabel = stringResource(R.string.shortcut_new_user_button),
            iconRes = R.drawable.perfil1
        ),
        ShortcutItem(
            title = stringResource(R.string.shortcut_care_guides_title),
            description = "",
            buttonLabel = stringResource(R.string.shortcut_care_guides_button),
            iconRes = R.drawable.guide1
        )
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                currentRoute = "main",
                onNavigate = onNavigate,
                onClose = { scope.launch { drawerState.close() } },
                onLogout = { loginViewModel.logout() }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = stringResource(R.string.home_title),
                    onNavigationClick = { scope.launch { drawerState.open() } }
                )
            },
            containerColor = Color(0xFFF4ECEC)
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                // Banner Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color(0xFFD6C2C2)),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.home_banner_title),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color(0xFF4A1B2A)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(R.string.home_banner_subtitle),
                                fontSize = 14.sp,
                                color = Color(0xFF4A1B2A)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { onNavigate("subscriptions") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4A1B2A),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(stringResource(R.string.home_banner_button), fontSize = 14.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Image(
                            painter = painterResource(id = R.drawable.coheteespacial1),
                            contentDescription = stringResource(R.string.home_banner_image_desc),
                            modifier = Modifier.size(120.dp)
                        )
                    }
                }

                // Shortcuts Section (LazyColumn)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp, vertical = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(25.dp)
                ) {
                    items(shortcuts) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = item.title,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF4A1B2A)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Button(
                                        onClick = { /* TODO: navigation logic */ },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF4A1B2A),
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(20.dp),
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                                    ) {
                                        Text(item.buttonLabel, fontSize = 13.sp)
                                    }
                                }

                                Image(
                                    painter = painterResource(id = item.iconRes),
                                    contentDescription = item.title,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
