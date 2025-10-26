
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import alerts.data.remote.repositories.AlertsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val repository: AlertsRepository
) : ViewModel() {

    private val _alerts = MutableStateFlow<List<Alert>>(emptyList())
    val alerts: StateFlow<List<Alert>> = _alerts

    // Puedes cambiar el accountId por el valor real que necesites
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
