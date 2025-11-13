package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.presentation.subscriptions.components

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.presentation.plan.PlanViewModel
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.presentation.subscriptions.SubscriptionsViewModel

@Composable
fun AvailablePlansSection(
    currentPlanType: String,
    planViewModel: PlanViewModel = hiltViewModel(),
    subscriptionsViewModel: SubscriptionsViewModel = hiltViewModel(),
) {
    val allPlans by planViewModel.plans.collectAsState()
    val isLoading by planViewModel.isLoading.collectAsState()
    val subscription by subscriptionsViewModel.subscriptions.collectAsState()

    val context = LocalContext.current

    val availablePlans = remember(currentPlanType, allPlans) {
        when (currentPlanType) {
            "Free" -> allPlans.filter { it.planType == "Premium" || it.planType == "Enterprise" }
            "Premium" -> allPlans.filter { it.planType == "Enterprise" }
            else -> emptyList()
        }
    }

    val (selectedPlanId, setSelectedPlanId) = remember {
        androidx.compose.runtime.mutableStateOf<String?>(null)
    }

    LaunchedEffect(subscription) {
        subscription?.let {
            if (!it.initPoint.isNullOrBlank()) {
                val intent = CustomTabsIntent.Builder().build()
                intent.launchUrl(context, it.initPoint.toUri())
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFFFF6B35),
                strokeWidth = 3.dp
            )
        }
        return
    }

    if (availablePlans.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Celebration,
                    contentDescription = "Highest Plan",
                    tint = Color(0xFFFFA726),
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.label_highest_plan),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${stringResource(R.string.label_highest_plan_thanks)}.",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }

    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {

            Text(
                text = stringResource(R.string.label_update_plan),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            availablePlans.forEach { plan ->
                SelectablePlanCard(
                    plan = plan,
                    isSelected = plan.id == selectedPlanId,
                    onSelect = { setSelectedPlanId(plan.id) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val subscriptionId = subscriptionsViewModel.accountSubscriptions.value?.subscriptionId ?: ""
                    val selectedPlan = availablePlans.find { it.id == selectedPlanId }
                    selectedPlan?.let {
                        subscriptionsViewModel.upgradeSubscription(
                            subscriptionId = subscriptionId,
                            newPlanId = it.id ?: ""
                        )
                    }
                },
                enabled = selectedPlanId != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B35),
                    disabledContainerColor = Color(0xFF4A1520)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_update),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}