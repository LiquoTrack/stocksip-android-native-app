package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.presentation.subscriptions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.features.authentication.login.presentation.login.LoginViewModel
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.presentation.subscriptions.components.AvailablePlansSection
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.presentation.subscriptions.components.SubscriptionPlanCard
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import com.liquotrack.stocksip.shared.ui.components.TopBar
import kotlinx.coroutines.launch

@Composable
fun AccountSubscriptionPlanView(
    subscriptionsViewModel: SubscriptionsViewModel = hiltViewModel(),
    onLogout: () -> Unit = {},
    loginViewModel: LoginViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit = {},
) {

    val isLoading by subscriptionsViewModel.isLoading.collectAsState()

    val accountSubscription by subscriptionsViewModel.accountSubscriptions.collectAsState()

    val scope = rememberCoroutineScope()
    val isLoggedOut by loginViewModel.isLoggedOut.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    LaunchedEffect(isLoggedOut) {
        if (isLoggedOut) {
            onLogout()
            loginViewModel.resetLogoutState()
        }
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
                onLogout = { loginViewModel.logout() }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = "Subscriptions Plan",
                    onNavigationClick = { scope.launch { drawerState.open() } }
                )
            },
            containerColor = Color(0xFFF4ECEC)

        ) { padding ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF4A1B2A))
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    accountSubscription?.let { subscription ->
                        val formattedSubscription = subscriptionsViewModel.formatSubscription(subscription)
                        SubscriptionPlanCard(accountSubscription = formattedSubscription)

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            "Update Your plan",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF4A1B2A)
                        )

                        AvailablePlansSection(currentPlanType = subscription.planType)
                    }
                }
            }
        }
    }
}