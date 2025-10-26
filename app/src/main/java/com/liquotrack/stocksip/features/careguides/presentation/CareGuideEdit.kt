package com.liquotrack.stocksip.features.careguides.presentation


import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.features.careguides.domain.CareGuide
import kotlinx.coroutines.launch

private val BackgroundColor = Color(0xFFFDF3EA)
private val AppBarColor = Color(0xFFFDEFE6)
private val AccentColor = Color(0xFF4A1B2A)
private val FieldColor = Color(0xFFF6E8D7)
private val PlaceholderColor = Color(0xFF8E8C89)
private val IllustrationBorderColor = Color(0xFFE1CBC1)
private val IllustrationBackgroundColor = Color(0xFFF5E6EC)
private val DeleteColor = Color(0xFFE53E3E)
private val CancelColor = Color(0xFFE6E0DC)

@Composable
fun CareGuideEdit(
    careGuideId: String,
    onNavigateBack: () -> Unit,
    onDeleted: () -> Unit,
    viewModel: CareGuideEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val formState = rememberCareGuideEditFormState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(careGuideId) {
        formState.reset()
        viewModel.loadCareGuide(careGuideId)
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is CareGuideEditUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }

            CareGuideEditUiState.Deleted -> {
                Toast.makeText(context, "Care guide deleted", Toast.LENGTH_SHORT).show()
                onDeleted()
            }

            is CareGuideEditUiState.Loaded -> {
                formState.populateFrom(state.careGuide)
            }

            else -> Unit
        }
    }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            CareGuideEditTopBar(
                title = if (formState.productName.isNotBlank()) formState.productName else "Care Guide",
                enableDelete = uiState is CareGuideEditUiState.Loaded,
                onNavigateBack = onNavigateBack,
                onDeleteClick = { showDeleteDialog = true }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            CareGuideEditUiState.Loading -> CareGuideEditLoading(padding)
            is CareGuideEditUiState.Error -> CareGuideEditError(
                padding = padding,
                onRetry = { viewModel.loadCareGuide(careGuideId) }
            )
            is CareGuideEditUiState.Loaded -> CareGuideEditFormContent(
                padding = padding,
                scrollState = scrollState,
                formState = formState,
                onSave = {
                    val updated = formState.buildUpdatedCareGuide(state.careGuide)
                    if (updated == null) {
                        Toast.makeText(context, "Enter valid temperatures.", Toast.LENGTH_SHORT).show()
                    } else {
                        scope.launch {
                            val success = viewModel.updateCareGuide(updated)
                            if (success) {
                                Toast.makeText(context, "Guide updated successfully.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            )
            CareGuideEditUiState.Deleted -> Unit
        }
    }

    if (showDeleteDialog) {
        CareGuideDeleteDialog(
            onConfirm = {
                scope.launch {
                    val success = viewModel.deleteCareGuideSuspending(careGuideId)
                    if (success) {
                        showDeleteDialog = false
                        Toast.makeText(context, "Guide deleted successfully.", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

@Composable
private fun CareGuideEditTopBar(
    title: String,
    enableDelete: Boolean,
    onNavigateBack: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Surface(color = AppBarColor, shadowElevation = 4.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = AccentColor
                )
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = AccentColor,
                    fontWeight = FontWeight.Medium
                )
            }

            IconButton(
                onClick = onDeleteClick,
                enabled = enableDelete
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = AccentColor.copy(alpha = if (enableDelete) 1f else 0.4f)
                )
            }
        }
    }
}

@Composable
private fun CareGuideEditLoading(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = AccentColor)
    }
}

@Composable
private fun CareGuideEditError(
    padding: PaddingValues,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "The guide could not be loaded.",
                color = AccentColor,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = AccentColor, contentColor = Color.White)
            ) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun CareGuideEditFormContent(
    padding: PaddingValues,
    scrollState: ScrollState,
    formState: CareGuideEditFormState,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        CareGuideEditIllustration(formState.imageUrl)

        Spacer(modifier = Modifier.height(24.dp))

        CareGuideEditField(
            value = formState.title,
            onValueChange = { formState.title = it },
            placeholder = "Title"
        )

        Spacer(modifier = Modifier.height(12.dp))

        CareGuideEditField(
            value = formState.productName,
            onValueChange = { formState.productName = it },
            placeholder = "Product Name"
        )

        Spacer(modifier = Modifier.height(12.dp))

        CareGuideEditField(
            value = formState.summary,
            onValueChange = { formState.summary = it },
            placeholder = "Comments",
            singleLine = false
        )

        Spacer(modifier = Modifier.height(12.dp))

        CareGuideEditField(
            value = formState.minTemp,
            onValueChange = { formState.minTemp = it },
            placeholder = "Min. Temperature"
        )

        Spacer(modifier = Modifier.height(12.dp))

        CareGuideEditField(
            value = formState.maxTemp,
            onValueChange = { formState.maxTemp = it },
            placeholder = "Max. Temperature"
        )

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = onSave,
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(0.6f),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentColor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = "Save",
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
private fun rememberCareGuideEditFormState(): CareGuideEditFormState {
    return remember { CareGuideEditFormState() }
}

private class CareGuideEditFormState {
    var productName by mutableStateOf("")
    var type by mutableStateOf("")
    var title by mutableStateOf("")
    var summary by mutableStateOf("")
    var minTemp by mutableStateOf("")
    var maxTemp by mutableStateOf("")
    var imageUrl by mutableStateOf("")
    var recommendedPlaceStorage by mutableStateOf("")
    var generalRecommendation by mutableStateOf("")

    private var initialized by mutableStateOf(false)

    fun populateFrom(careGuide: CareGuide) {
        if (initialized) return
        productName = careGuide.productName
        type = careGuide.productAssociated
        title = careGuide.title
        summary = careGuide.summary
        minTemp = careGuide.recommendedMinTemperature.toString()
        maxTemp = careGuide.recommendedMaxTemperature.toString()
        imageUrl = careGuide.imageUrl
        recommendedPlaceStorage = careGuide.recommendedPlaceStorage
        generalRecommendation = careGuide.generalRecommendation
        initialized = true
    }

    fun reset() {
        productName = ""
        type = ""
        title = ""
        summary = ""
        minTemp = ""
        maxTemp = ""
        imageUrl = ""
        recommendedPlaceStorage = ""
        generalRecommendation = ""
        initialized = false
    }

    fun buildUpdatedCareGuide(base: CareGuide): CareGuide? {
        val minValue = minTemp.toDoubleOrNull()
        val maxValue = maxTemp.toDoubleOrNull()
        if (minValue == null || maxValue == null) {
            return null
        }
        return base.copy(
            productName = productName,
            title = title,
            productAssociated = type,
            summary = summary,
            recommendedMinTemperature = minValue,
            recommendedMaxTemperature = maxValue,
            recommendedPlaceStorage = recommendedPlaceStorage.ifBlank { base.recommendedPlaceStorage },
            generalRecommendation = generalRecommendation.ifBlank { base.generalRecommendation }
        )
    }
}

@Composable
private fun CareGuideEditIllustration(imageUrl: String) {
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
            if (imageUrl.isNotBlank()) {
                val context = LocalContext.current
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .clip(RoundedCornerShape(16.dp)),
                    alignment = Alignment.Center
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
            }
        }
    }
}

@Composable
private fun CareGuideEditField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        modifier = Modifier
            .fillMaxWidth()
            .height(if (singleLine) 58.dp else 116.dp),
        placeholder = { Text(placeholder, color = PlaceholderColor) },
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
            cursorColor = AccentColor
        )
    )
}

@Composable
private fun CareGuideDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Are you sure you want to delete this care guide?",
                    color = AccentColor,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeleteColor,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Delete")
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CancelColor,
                        contentColor = AccentColor
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}
