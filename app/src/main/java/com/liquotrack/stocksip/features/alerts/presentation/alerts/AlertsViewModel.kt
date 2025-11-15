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

    private val _uiState = MutableStateFlow(AlertsUiState())
    val uiState: StateFlow<AlertsUiState> = _uiState.asStateFlow()

    private val _alertsToShowInOverlay = MutableStateFlow<List<Alert>>(emptyList())
    val alertsToShowInOverlay: StateFlow<List<Alert>> = _alertsToShowInOverlay.asStateFlow()


    fun loadAlerts(accountId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val fetchedAlerts = repository.fetchAlerts(accountId)

                _uiState.update {
                    it.copy(isLoading = false, alerts = fetchedAlerts)
                }

                _alertsToShowInOverlay.value = fetchedAlerts

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Error al cargar alertas: ${e.message}")
                }
            }
        }
    }

    fun dismissOverlay() {
        _alertsToShowInOverlay.value = emptyList()
    }

}