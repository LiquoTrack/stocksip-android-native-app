package com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.presentation.plan

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.domain.models.Plan

@Composable
fun PlanCard(
    plan: Plan,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val borderColor = if (isSelected) Color(0xFFE8B4A8) else Color.Transparent
    val backgroundColor = Color(0xFFE8CCC6)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(3.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onSelect() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Título del plan
            Text(
                text = plan.planType,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D0818),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Precio
            val priceText = when {
                plan.planPrice.contains("0") && plan.paymentFrequency == "None" -> "Free"
                else -> {
                    val price = plan.planPrice.replace(" USD", "")
                    val frequency = when (plan.paymentFrequency) {
                        "Monthly" -> "/month"
                        "Yearly" -> "/year"
                        else -> ""
                    }
                    "s/. $price$frequency"
                }
            }

            Text(
                text = priceText,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4A1426),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Descripción
            if (!plan.description.isNullOrEmpty()) {
                Text(
                    text = plan.description,
                    fontSize = 13.sp,
                    color = Color(0xFF4A1426),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Características basadas en planLimits
            plan.planLimits?.let { limits ->
                limits.maxWarehouses?.let {
                    val warehouseText = if (it == -1) "Unlimited warehouses"
                    else "Up to $it warehouse${if (it > 1) "s" else ""}"
                    BulletPoint(warehouseText)
                }
                limits.maxProducts?.let {
                    val productText = if (it == -1) "Unlimited products"
                    else "Up to $it product${if (it > 1) "s" else ""}"
                    BulletPoint(productText)
                }
                if (limits.storageGuides == true) {
                    BulletPoint("Storage guides")
                }
                if (limits.premiumStorageGuides == true) {
                    BulletPoint("Premium storage guides")
                }
                if (limits.communitySupport == true) {
                    BulletPoint("Community support")
                }
                if (limits.prioritySupport == true) {
                    BulletPoint("Priority support")
                }
            }
        }
    }
}

@Composable
fun BulletPoint(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = "• ",
            color = Color(0xFF4A1426),
            fontSize = 14.sp
        )
        Text(
            text = text,
            color = Color(0xFF4A1426),
            fontSize = 14.sp
        )
    }
}