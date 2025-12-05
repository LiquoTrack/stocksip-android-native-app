package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.presentation.subscriptions

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.features.authentication.login.presentation.login.LoginViewModel
import com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.presentation.account.AccountViewModel
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.presentation.subscriptions.components.AvailablePlansSection
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.presentation.subscriptions.components.SubscriptionPlanCard
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import kotlinx.coroutines.launch

@Composable
fun AccountSubscriptionPlanView(
    subscriptionsViewModel: SubscriptionsViewModel = hiltViewModel(),
    onLogout: () -> Unit = {},
    loginViewModel: LoginViewModel = hiltViewModel(),
    accountViewModel: AccountViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit = {},
) {

    val isLoading by subscriptionsViewModel.isLoading.collectAsState()
    val accountSubscription by subscriptionsViewModel.accountSubscriptions.collectAsState()

    val scope = rememberCoroutineScope()
    val isLoggedOut by loginViewModel.isLoggedOut.collectAsState()
    val userRole by accountViewModel.accountRole.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            onLogout()
            loginViewModel.resetLogoutState()
        }
    }

    LaunchedEffect(userRole) {
        if (userRole == null) accountViewModel.loadAccountRoleFromStorage()
    }

    LaunchedEffect(Unit) {
        subscriptionsViewModel.fetchAccountSubscription()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                currentRoute = "subscription",
                onNavigate = onNavigate,
                onClose = { scope.launch { drawerState.close() } },
                onLogout = { loginViewModel.logout() },
                userRole = userRole
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF2B000D),
                            Color(0xFF5E2430),
                            Color(0xFF914852)
                        )
                    )
                )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { scope.launch { drawerState.open() } },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                Color.White.copy(alpha = 0.15f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = stringResource(R.string.label_subscriptions),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )

                    Box(modifier = Modifier.size(48.dp))
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFFF6B35),
                            strokeWidth = 3.dp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Subscription Card
                        item {
                            accountSubscription?.let { subscription ->
                                val formattedSubscription = subscriptionsViewModel.formatSubscription(subscription)
                                SubscriptionPlanCard(accountSubscription = formattedSubscription)
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Available Plans Section
                        item {
                            accountSubscription?.let { subscription ->
                                AvailablePlansSection(currentPlanType = subscription.planType)
                            }
                        }
                    }
                }
            }
        }
    }
}