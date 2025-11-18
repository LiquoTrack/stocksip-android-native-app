package com.liquotrack.stocksip.shared.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A custom spinner field implemented using Jetpack Compose.
 *
 * @param items The list of string items to display in the spinner.
 * @param onItemSelected A callback function that is invoked when an item is selected.
 * @param modifier Modifier to be applied to the spinner field.
 * @param isRequired Boolean indicating if the field is required (adds an asterisk to the label).
 * @param label The label to display above the spinner field.
 */
@Composable
fun CustomSpinnerField(
    items: List<String>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }
    val isEmpty = items.isEmpty()
    var selectedItem by remember(items) { mutableStateOf(items.firstOrNull() ?: "") }

    LaunchedEffect(items) {
        items.firstOrNull()?.let {
            selectedItem = it
            onItemSelected(it)
        } ?: run {
            selectedItem = ""
        }
    }

    // QUITAR el Box con height fijo y usar Column en su lugar
    Column(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = selectedItem,
            onValueChange = { },
            readOnly = true,
            label = { Text(if (isRequired) "$label *" else label) },
            trailingIcon = {
                IconButton(onClick = { if (!isEmpty) expanded = !expanded }) {
                    val icon =
                        if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
                    Icon(imageVector = icon, contentDescription = null)
                }
            },
            enabled = !isEmpty,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        selectedItem = item
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }

        if (isEmpty) {
            Text(
                text = "No options available",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}