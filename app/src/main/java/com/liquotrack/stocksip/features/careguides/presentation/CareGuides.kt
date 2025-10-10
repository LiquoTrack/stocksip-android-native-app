package com.liquotrack.stocksip.features.careguides.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.core.navigation.Route
import com.liquotrack.stocksip.features.careguides.domain.CareGuide
import com.liquotrack.stocksip.shared.ui.components.NavDrawer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareGuides(
    onNavigate: (String) -> Unit = {},
    viewModel: CareGuideViewModel = hiltViewModel()
) {
    val search = remember { mutableStateOf("") }
    val careGuides = viewModel.careGuides.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedGuide by remember { mutableStateOf<CareGuide?>(null) }
    val showDialog = selectedGuide != null

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                currentRoute = "care_guide",
                onNavigate = onNavigate,
                onClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Care Guides",
                            color = Color(0xFF4A1B2A),
                            fontWeight = FontWeight.Medium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color(0xFF4A1B2A)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFF4ECEC)
                    )
                )
            },
            containerColor = Color(0xFFF4ECEC)
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = search.value,
                        onValueChange = { search.value = it },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null)
                        },
                        placeholder = {
                            Text(stringResource(R.string.placeholder_search))
                        },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = { onNavigate(Route.CareGuideCreate.route) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53E3E),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Text("New")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(careGuides.value.size) { index ->
                        val careGuide = careGuides.value[index]
                        CareGuideCard(
                            careGuide = careGuide,
                            onClick = {},
                            onSeeGuide = { selectedGuide = it }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        CareGuideDetailDialog(
            careGuide = selectedGuide!!,
            onDismiss = { selectedGuide = null }
        )
    }
}

@Composable
private fun CareGuideDetailDialog(
    careGuide: CareGuide,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (careGuide.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(careGuide.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = careGuide.productName,
                        modifier = Modifier
                            .height(140.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .height(140.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFE9D9CA)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalDrink,
                            contentDescription = careGuide.productName,
                            tint = Color(0xFF8A3040)
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    DetailRow(title = "Product Name", value = careGuide.productName)
                    DetailRow(title = "Type", value = careGuide.title.ifBlank { "N/A" })
                    DetailRow(title = "Comments", value = careGuide.summary)
                    DetailRow(title = "Min. Temperature", value = "${careGuide.recommendedMinTemperature}° C")
                    DetailRow(title = "Max. Temperature", value = "${careGuide.recommendedMaxTemperature}° C")
                }

                TextButton(onClick = onDismiss) {
                    Text(
                        text = "Close",
                        color = Color(0xFF8A3040),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(title: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, fontWeight = FontWeight.SemiBold, color = Color(0xFF3B2B2B))
        Text(value, color = Color(0xFF737373))
    }
}