package com.liquotrack.stocksip.features.careguides.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.liquotrack.stocksip.R

private val BackgroundColor = Color(0xFFFDF3EA)
private val AppBarColor = Color(0xFFFDEFE6)
private val AccentColor = Color(0xFF4A1B2A)
private val FieldColor = Color(0xFFF6E8D7)
private val PlaceholderColor = Color(0xFF8E8C89)
private val IllustrationBorderColor = Color(0xFFE1CBC1)
private val IllustrationBackgroundColor = Color(0xFFF5E6EC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareGuideCreate(
    onNavigateBack: () -> Unit = {}
) {
    var product by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf("") }
    var minTemp by remember { mutableStateOf("") }
    var maxTemp by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "New Guide",
                        color = AccentColor,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = AccentColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBarColor
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            CareGuideIllustration()

            Spacer(modifier = Modifier.height(24.dp))

            CareGuideInputField(
                value = product,
                onValueChange = { product = it },
                placeholder = "Select Product",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = AccentColor
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            CareGuideInputField(
                value = type,
                onValueChange = { type = it },
                placeholder = "Type"
            )

            Spacer(modifier = Modifier.height(12.dp))

            CareGuideInputField(
                value = comments,
                onValueChange = { comments = it },
                placeholder = "Comments",
                singleLine = false
            )

            Spacer(modifier = Modifier.height(12.dp))

            CareGuideInputField(
                value = minTemp,
                onValueChange = { minTemp = it },
                placeholder = "Min. Temperature"
            )

            Spacer(modifier = Modifier.height(12.dp))

            CareGuideInputField(
                value = maxTemp,
                onValueChange = { maxTemp = it },
                placeholder = "Max. Temperature"
            )

            Spacer(modifier = Modifier.height(36.dp))

            Button(
                onClick = { /* TODO: handle submission */ },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(48.dp)
                    .width(160.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 6.dp)
            ) {
                Text(
                    text = "Add",
                    fontWeight = FontWeight.SemiBold
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
                contentDescription = "Guide illustration",
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