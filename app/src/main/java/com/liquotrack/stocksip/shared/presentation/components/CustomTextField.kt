package com.liquotrack.stocksip.shared.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A customizable text field component with label, placeholder, and error handling.
 *
 * @param value The current text value of the text field.
 * @param onValueChange Callback function to handle text changes.
 * @param label The label to display above the text field.
 * @param placeholder The placeholder text to display when the text field is empty.
 * @param modifier Modifier to be applied to the text field.
 * @param keyboardType The type of keyboard to use for the text field.
 * @param isRequired Boolean indicating if the field is required (adds an asterisk to the label).
 * @param showError Boolean indicating if an error state should be shown.
 */
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isRequired: Boolean = false,
    showError: Boolean = false
) {
    Column(modifier = modifier) {
        Text(
            text = if (isRequired) "$label *" else label,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.Gray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = if (showError && value.isBlank()) Color.Red else Color(0xFF2B000D),
                unfocusedBorderColor = if (showError && value.isBlank()) Color.Red else Color.LightGray,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )

        if (showError && value.isBlank() && isRequired) {
            Text(
                text = "This field is required",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}