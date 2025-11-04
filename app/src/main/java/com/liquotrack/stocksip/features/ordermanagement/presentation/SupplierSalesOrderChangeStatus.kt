package com.liquotrack.stocksip.features.ordermanagement.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SupplierSalesOrderChangeStatus(
    isVisible: Boolean,
    currentStatus: String,
    options: List<String> = listOf("PENDING", "CONFIRM", "CANCEL"),
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isVisible) return

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Text(
                text = "Change Order Status",
                color = Color(0xFF6B3B44),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                fun toAlias(status: String): String {
                    return when (status.trim().uppercase()) {
                        "PROCESSING", "RECEIVED" -> "PENDING"
                        "CONFIRMED" -> "CONFIRM"
                        "CANCELED" -> "CANCEL"
                        else -> status.trim().uppercase()
                    }
                }
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                onSelect(option)
                                onDismiss()
                            }
                    ) {
                        Checkbox(
                            checked = toAlias(currentStatus) == option,
                            onCheckedChange = {
                                onSelect(option)
                                onDismiss()
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF6B3B44)
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option, fontSize = 16.sp, color = Color(0xFF4A1B2A))
                    }
                }
            }
        }
    )
}