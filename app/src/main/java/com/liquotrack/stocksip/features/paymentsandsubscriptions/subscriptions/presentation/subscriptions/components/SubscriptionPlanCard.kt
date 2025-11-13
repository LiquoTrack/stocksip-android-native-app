package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.presentation.subscriptions.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.models.AccountSubscription
import com.liquotrack.stocksip.R

@Composable
fun SubscriptionPlanCard(
    accountSubscription: AccountSubscription
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Plan Type
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${stringResource(R.string.label_your_current_plan)}:",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color(0xFF4A1B2A)
                )
                Text(
                    text = accountSubscription.planType,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${stringResource(R.string.label_status)}:",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = Color(0xFF4A1B2A)
                )
                Text(
                    text = accountSubscription.status,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (accountSubscription.status.lowercase() == "active")
                        Color(0xFF2E7D32)
                    else Color(0xFFC62828)
                )
            }

            // Expiration
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${stringResource(R.string.label_expiration_date)}:",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = accountSubscription.expirationDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Column {
                Text(
                    text = "${stringResource(R.string.label_plan_benefits)}:",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color(0xFF4A1B2A)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("• ${stringResource(R.string.label_max_users_allowed)}: ${accountSubscription.maxUsers}", style = MaterialTheme.typography.bodySmall)
                    Text("• ${stringResource(R.string.label_max_products_allowed)}: ${accountSubscription.maxProducts}", style = MaterialTheme.typography.bodySmall)
                    Text("• ${stringResource(R.string.label_max_warehouses_allowed)}: ${accountSubscription.maxWarehouses}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}