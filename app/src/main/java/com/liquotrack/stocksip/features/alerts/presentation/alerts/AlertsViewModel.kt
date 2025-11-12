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

// 1. Define el estado de tu UI
data class AlertsUiState(
    val isLoading: Boolean = false,
    val alerts: List<Alert> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val repository: AlertsRepository
) : ViewModel() {

    // 2. Crea el StateFlow para el estado
    private val _uiState = MutableStateFlow(AlertsUiState())
    val uiState: StateFlow<AlertsUiState> = _uiState.asStateFlow()

    // 3. Función para cargar las alertas
    fun loadAlerts(accountId: String) {
        viewModelScope.launch {
            // Pone el estado en "cargando"
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // Llama al repositorio
                val fetchedAlerts = repository.fetchAlerts(accountId)

                // Actualiza el estado con éxito
                _uiState.update {
                    it.copy(isLoading = false, alerts = fetchedAlerts)
                }
            } catch (e: Exception) {
                // Actualiza el estado con error
                _uiState.update {
                    it.copy(isLoading = false, error = "Error al cargar alertas: ${e.message}")
                }
            }
        }
    }
}