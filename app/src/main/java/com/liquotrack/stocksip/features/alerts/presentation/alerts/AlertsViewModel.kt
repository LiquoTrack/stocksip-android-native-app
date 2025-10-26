package alerts.presentation.alerts

import Alert
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import alerts.data.remote.repositories.AlertsRepository
import kotlinx.coroutines.launch

class AlertsViewModel(
    private val repository: AlertsRepository
) : ViewModel() {

    val alerts = MutableLiveData<List<Alert>>()
    val error = MutableLiveData<String>()

    fun loadAlerts(accountId: String) {
        viewModelScope.launch {
            try {
                val result = repository.fetchAlerts(accountId)
                alerts.value = result
            } catch (e: Exception) {
                error.value = e.message ?: "Unknown error"
            }
        }
    }
}
