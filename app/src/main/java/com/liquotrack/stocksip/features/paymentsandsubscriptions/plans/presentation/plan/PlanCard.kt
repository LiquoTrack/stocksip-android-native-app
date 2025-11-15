package com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.presentation.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.domain.models.Plan
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PlanCard(
    plan: Plan,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val isPopular = plan.paymentFrequency == "Monthly"
    val borderColor = if (isSelected) Color(0xFFFF6B35) else Color(0xFF3A1520)
    val backgroundColor = when {
        isPopular -> Brush.verticalGradient(
            listOf(
                Color(0xFFFFA726),
                Color(0xFFFF8A50)
            )
        )
        else -> Brush.verticalGradient(
            listOf(
                Color(0xFF1A0810),
                Color(0xFF2D1520)
            )
        )
    }

    val textColor = if (isPopular) Color(0xFF2B000D) else Color.White

    Box {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (isPopular) 8.dp else 0.dp)
                .shadow(
                    elevation = if (isSelected) 20.dp else 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = if (isSelected) Color(0xFFFF6B35) else Color.Black.copy(alpha = 0.3f)
                )
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(24.dp)
                )
                .clickable { onSelect() },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(28.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Plan Type
                    Text(
                        text = plan.planType.uppercase(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Price
                    val priceMatch = Regex("""(\d+\.?\d*)""").find(plan.planPrice)
                    val price = priceMatch?.value?.toDoubleOrNull() ?: 0.0
                    val priceText = formatPrice(price)

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = priceText,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = textColor
                        )

                        Text(
                            text = when (plan.paymentFrequency) {
                                "Monthly" -> stringResource(R.string.label_payment_frequency_monthly)
                                "Yearly" -> stringResource(R.string.label_payment_frequency_yearly)
                                else -> ""
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = textColor.copy(alpha = 0.7f),
                            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                        )
                    }

                    // Discount badge para yearly
                    if (plan.paymentFrequency == "Yearly") {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFFF6B35),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${stringResource(R.string.label_save_money)} 39%!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Description
                    if (!plan.description.isNullOrEmpty()) {
                        Text(
                            text = plan.description,
                            fontSize = 13.sp,
                            color = textColor.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 20.dp)
                        )
                    }

                    // Divider
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(1.dp)
                            .background(textColor.copy(alpha = 0.2f))
                            .padding(vertical = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Features
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        plan.planLimits?.let { limits ->
                            limits.maxUsers?.let {
                                val userText = if (it == Int.MAX_VALUE || it >= 2147483647) "Unlimited users"
                                else "${stringResource(R.string.label_up_to)} $it ${stringResource(R.string.label_up_to_users)}"
                                FeatureItem(userText, textColor)
                            }
                            limits.maxWarehouses?.let {
                                val warehouseText = if (it == Int.MAX_VALUE || it >= 2147483647) "Unlimited warehouses"
                                else "${stringResource(R.string.label_up_to)} $it ${stringResource(R.string.label_up_to_warehouses)}"
                                FeatureItem(warehouseText, textColor)
                            }
                            limits.maxProducts?.let {
                                val productText = if (it == Int.MAX_VALUE || it >= 2147483647) "Unlimited products"
                                else "${stringResource(R.string.label_up_to)} $it ${stringResource(R.string.label_up_to_products)}"
                                FeatureItem(productText, textColor)
                            }
                            if (limits.storageGuides == true) {
                                FeatureItem("Storage guides", textColor)
                            }
                            if (limits.premiumStorageGuides == true) {
                                FeatureItem("Premium storage guides", textColor)
                            }
                            if (limits.communitySupport == true) {
                                FeatureItem("Community support", textColor)
                            }
                            if (limits.prioritySupport == true) {
                                FeatureItem("Priority support", textColor)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureItem(text: String, textColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

private fun formatPrice(price: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    return formatter.format(price)
}