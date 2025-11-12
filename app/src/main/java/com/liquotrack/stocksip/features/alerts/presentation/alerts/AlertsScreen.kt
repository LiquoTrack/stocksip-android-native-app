package com.liquotrack.stocksip.features.alerts.presentation.alerts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.liquotrack.stocksip.features.alerts.presentation.alerts.components.AlertCard

@Composable
fun AlertsScreen(
    viewModel: AlertsViewModel = hiltViewModel(),
    // Deberías pasar el accountId a tu pantalla, por ejemplo, desde la navegación
    accountId: String
) {
    // 1. Llama a la función del ViewModel para cargar los datos
    // LaunchedEffect se asegura de que se llame solo una vez
    LaunchedEffect(key1 = accountId) {
        viewModel.loadAlerts(accountId)
    }

    // 2. Observa el estado del ViewModel
    val state by viewModel.uiState.collectAsState()

    // 3. Dibuja la UI según el estado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        when {
            // --- Estado de Carga ---
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // --- Estado de Error ---
            state.error != null -> {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // --- Estado de Éxito ---
            else -> {
                // Usamos LazyColumn para mostrar una lista de Alertas
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    // Itera sobre la lista de alertas del estado
                    items(state.alerts) { alert ->
                        // Aquí usas tu AlertCard, que ya está hecho
                        AlertCard(alert = alert)
                    }
                }
            }
        }
    }
}