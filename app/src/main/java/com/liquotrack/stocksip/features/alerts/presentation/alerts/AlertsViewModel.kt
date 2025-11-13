package com.liquotrack.stocksip.features.alerts.presentation.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.alerts.data.remote.models.Alert
import com.liquotrack.stocksip.features.alerts.data.repositories.AlertsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlertsUiState(
    val isLoading: Boolean = false,
    val alerts: List<Alert> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val repository: AlertsRepository
) : ViewModel() {

    // 1. Estado para la AlertsScreen (la lista completa)
    private val _uiState = MutableStateFlow(AlertsUiState())
    val uiState: StateFlow<AlertsUiState> = _uiState.asStateFlow()

    // --- ESTA ES LA PARTE NUEVA QUE TE FALTA ---

    // 2. Estado separado para las alertas del Overlay
    private val _alertsToShowInOverlay = MutableStateFlow<List<Alert>>(emptyList())
    val alertsToShowInOverlay: StateFlow<List<Alert>> = _alertsToShowInOverlay.asStateFlow()

    // --- FIN DE LA PARTE NUEVA ---

    fun loadAlerts(accountId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val fetchedAlerts = repository.fetchAlerts(accountId)

                // Actualiza el estado de la pantalla de Alertas
                _uiState.update {
                    it.copy(isLoading = false, alerts = fetchedAlerts)
                }

                // --- ESTA ES LA LÍNEA NUEVA ---
                // También actualiza el estado del Overlay
                _alertsToShowInOverlay.value = fetchedAlerts

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Error al cargar alertas: ${e.message}")
                }
            }
        }
    }

    // --- ESTA ES LA FUNCIÓN NUEVA QUE TE FALTA ---

    // 3. Función para cerrar el Overlay
    fun dismissOverlay() {
        _alertsToShowInOverlay.value = emptyList()
    }

    // --- FIN DE LA FUNCIÓN NUEVA ---
}