package alerts.data.remote.services

import Alert
import retrofit2.http.GET
import retrofit2.http.Path

interface AlertsApiService {
    @GET("/api/v1/accounts/{accountId}/alerts")
    suspend fun getAlerts(
        @Path("accountId") accountId: String
    ): List<Alert>
}
