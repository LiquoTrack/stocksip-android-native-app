package com.liquotrack.stocksip.shared.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

/**
 * A composable function that provides a text input field for entering dates in the format dd-MM-yyyy.
 *
 * @param label The label to display for the input field. Default is "Expiration Date".
 * @param isRequired A boolean indicating whether the field is required. Default is false.
 * @param showError A boolean indicating whether to show an error message. Default is false.
 * @param errorMessage The error message to display if the field is required and left empty.
 * @param onDateChange A callback function that is invoked whenever the date input changes.
 */
@Composable
fun DateInputField(
    label: String = "Expiration Date",
    isRequired: Boolean = false,
    showError: Boolean = false,
    errorMessage: String = "This field is required",
    onDateChange: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    val labelText = buildAnnotatedString {
        append(label)
        if (isRequired) {
            withStyle(style = SpanStyle(color = Color.Red)) {
                append(" *")
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { input ->

                // Accept only digits and format as dd-MM-yyyy
                val digits = input.filter { it.isDigit() }
                val limited = digits.take(8)
                val formatted = buildString {
                    for (i in limited.indices) {
                        append(limited[i])
                        if (i == 1 || i == 3) append('-')
                    }
                }
                text = formatted
                onDateChange(formatted)
            },
            label = { Text(text = labelText) },
            placeholder = { Text("dd-MM-yyyy") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = showError && isRequired && text.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        // Show error message if needed
        if (showError && isRequired && text.isBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
        }
    }
}