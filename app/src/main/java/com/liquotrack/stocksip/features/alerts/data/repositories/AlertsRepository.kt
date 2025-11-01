package com.liquotrack.stocksip.features.alerts.data.repositories

import com.liquotrack.stocksip.features.alerts.data.remote.models.Alert
import com.liquotrack.stocksip.features.alerts.data.remote.services.AlertsApiService

class AlertsRepository(private val apiService: AlertsApiService) {

    // Método para obtener alertas por accountId desde el servicio de red
    suspend fun fetchAlerts(accountId: String): List<Alert> {
        return apiService.getAlerts(accountId)
    }
}
