package com.liquotrack.stocksip.features.inventorymanagement.careguides.presentation

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.shared.ui.components.TopBarWithBack

private val BackgroundColor = Color(0xFFF5EFED)
private val AppBarColor = Color.Transparent
private val AccentColor = Color(0xFF4A2B2B)
private val FieldColor = Color.White
private val PlaceholderColor = Color(0xFF8E8C89)
private val IllustrationBorderColor = Color(0xFFE1CBC1)
private val IllustrationBackgroundColor = Color(0xFFF0E0D8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareGuideCreate(
    onNavigateBack: () -> Unit = {},
    viewModel: CareGuideCreateViewModel = hiltViewModel()
) {
    var product by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf("") }
    var minTemp by remember { mutableStateOf("") }
    var maxTemp by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val bg = Color(0xFFF5EFED)

    val guideCreatedMessage = stringResource(R.string.guide_create)
    val newGuideTitle = stringResource(R.string.new_guide)
    val selectProductPlaceholder = stringResource(R.string.select_product)
    val typePlaceholder = stringResource(R.string.type)
    val commentsPlaceholder = stringResource(R.string.comments)
    val minTempPlaceholder = stringResource(R.string.min_temp)
    val maxTempPlaceholder = stringResource(R.string.max_temp)
    val addLabel = stringResource(R.string.add)

    val uiState by viewModel.uiState.collectAsState()
    val products by viewModel.products.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(uiState) {
        when (uiState) {
            CareGuideCreateUiState.Success -> {
                Toast.makeText(context, guideCreatedMessage, Toast.LENGTH_SHORT).show()
                product = ""
                type = ""
                comments = ""
                minTemp = ""
                maxTemp = ""
                viewModel.consumeState()
                onNavigateBack()
            }
            is CareGuideCreateUiState.Error -> {
                val message = (uiState as CareGuideCreateUiState.Error).message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.consumeState()
            }
            else -> Unit
        }
    }

    val isLoading = uiState is CareGuideCreateUiState.Loading

    Scaffold(
        containerColor = bg,
        topBar = {
            TopBarWithBack(
                title = newGuideTitle,
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                val productOptions = remember(products) { products.map { it.name }.sorted() }

                if (productOptions.isEmpty()) {
                    CareGuideInputField(
                        value = product,
                        onValueChange = { product = it },
                        placeholder = selectProductPlaceholder,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = AccentColor
                            )
                        }
                    )
                } else {
                    CareGuideProductSelector(
                        value = product,
                        onValueChange = { product = it },
                        placeholder = selectProductPlaceholder,
                        options = productOptions
                    )
                }

                CareGuideInputField(
                    value = type,
                    onValueChange = { type = it },
                    placeholder = typePlaceholder
                )

                CareGuideInputField(
                    value = comments,
                    onValueChange = { comments = it },
                    placeholder = commentsPlaceholder,
                    singleLine = false
                )

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    CareGuideInputField(
                        value = minTemp,
                        onValueChange = { minTemp = it },
                        placeholder = minTempPlaceholder,
                        modifier = Modifier.weight(1f)
                    )

                    CareGuideInputField(
                        value = maxTemp,
                        onValueChange = { maxTemp = it },
                        placeholder = maxTempPlaceholder,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.createCareGuide(
                        typeOfLiquor = type,
                        productName = product,
                        title = comments,
                        summary = comments,
                        minTemperature = minTemp,
                        maxTemperature = maxTemp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A2B2B)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = addLabel,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CareGuideProductSelector(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    options: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth()
                .height(58.dp),
            placeholder = { Text(placeholder, color = PlaceholderColor) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = FieldColor,
                unfocusedContainerColor = FieldColor,
                disabledContainerColor = FieldColor,
                errorContainerColor = FieldColor,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                focusedTextColor = AccentColor,
                unfocusedTextColor = AccentColor,
                cursorColor = AccentColor,
                focusedTrailingIconColor = AccentColor,
                unfocusedTrailingIconColor = AccentColor
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CareGuideInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        modifier = modifier
            .fillMaxWidth()
            .height(if (singleLine) 58.dp else 116.dp),
        placeholder = { Text(placeholder, color = PlaceholderColor) },
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = FieldColor,
            unfocusedContainerColor = FieldColor,
            disabledContainerColor = FieldColor,
            errorContainerColor = FieldColor,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
            focusedTextColor = AccentColor,
            unfocusedTextColor = AccentColor,
            cursorColor = AccentColor,
            focusedTrailingIconColor = AccentColor,
            unfocusedTrailingIconColor = AccentColor
        )
    )
}

@Composable
private fun CareGuideIllustration() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .aspectRatio(0.75f),
        shape = RoundedCornerShape(24.dp),
        color = IllustrationBackgroundColor,
        shadowElevation = 6.dp,
        border = BorderStroke(1.dp, IllustrationBorderColor)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(R.string.ilustration_guide),
                modifier = Modifier.fillMaxWidth(0.45f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CareGuideCreatePreview() {
    CareGuideCreate()
}