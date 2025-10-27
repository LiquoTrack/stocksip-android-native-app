package com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.presentation.plan

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.domain.models.Plan
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.presentation.subscriptions.SubscriptionsViewModel
import androidx.core.net.toUri

@Composable
fun ChoosePlanScreen(
    planViewModel: PlanViewModel = hiltViewModel(),
    subscriptionViewModel: SubscriptionsViewModel = hiltViewModel(),
    onContinue: (Plan?) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val plans by planViewModel.plans.collectAsState()
    val selectedPlan by planViewModel.selectedPlan.collectAsState()
    val planLoading by planViewModel.isLoading.collectAsState()
    val planErrorMessage by planViewModel.errorMessage.collectAsState()

    val context = LocalContext.current
    val subscription by subscriptionViewModel.subscriptions.collectAsState()
    val subscriptionLoading by subscriptionViewModel.isLoading.collectAsState()
    val subscriptionErrorMessage by subscriptionViewModel.errorMessage.collectAsState()

    LaunchedEffect(subscription?.preferenceId) {
        subscription?.preferenceId?.let { prefId ->
            subscriptionViewModel.fetchSubscriptionStatus(prefId) { status ->
                if (status == "Active") {
                    onContinue(selectedPlan)
                }
            }
        }
    }


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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Choose Your Plan",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )

                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(4.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFFFF6B35),
                                    Color(0xFFFFA726)
                                )
                            ),
                            shape = RoundedCornerShape(2.dp)
                        )
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            when {
                planLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFFF6B35),
                            strokeWidth = 3.dp
                        )
                    }
                }

                planErrorMessage != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = planErrorMessage ?: "Unknown error occurred",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { planViewModel.getAllPlans() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF6B35)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text(
                                text = "Retry",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                plans.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No plans available at the moment.",
                            color = Color.White.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(plans) { plan ->
                            PlanCard(
                                plan = plan,
                                isSelected = selectedPlan?.id == plan.id,
                                onSelect = { planViewModel.selectPlan(plan) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = {
                            selectedPlan?.let { plan ->
                                plan.id?.let { planId ->
                                    subscriptionViewModel.createInitialSubscription(planId)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6B35),
                            disabledContainerColor = Color(0xFF4A1520)
                        ),
                        shape = RoundedCornerShape(28.dp),
                        enabled = selectedPlan != null
                    ) {
                        Text(
                            text = "Continue",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}

