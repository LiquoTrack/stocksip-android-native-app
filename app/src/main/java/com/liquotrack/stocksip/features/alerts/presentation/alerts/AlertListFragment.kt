
package com.liquotrack.stocksip.features.alerts.presentation.alerts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.alerts.data.repositories.AlertsRepository
import com.liquotrack.stocksip.features.alerts.data.remote.models.Alert
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val repository: AlertsRepository
) : ViewModel() {

    private val _alerts = MutableStateFlow<List<Alert>>(emptyList<Alert>())
    val alerts: StateFlow<List<Alert>> = _alerts

    fun loadAlerts(accountId: String = "68e49ffad906e587b9a91e4b") {
        viewModelScope.launch {
            _alerts.value = repository.fetchAlerts(accountId)
            Log.d("ALERTS", "Recibidas: ${_alerts.value}" )
        }
    }

    init {
        loadAlerts()
    }
}
