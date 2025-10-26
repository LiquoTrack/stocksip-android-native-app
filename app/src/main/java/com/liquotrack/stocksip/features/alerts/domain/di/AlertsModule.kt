package alerts.domain.di

import alerts.data.remote.repositories.AlertsRepository
import alerts.data.remote.services.AlertsApiService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val alertsModule = module {
    // Instancia Retrofit básica (ajusta la base URL según tu backend)
    single {
        Retrofit.Builder()
            .baseUrl("http://localhost:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AlertsApiService::class.java)
    }

    // Repositorio inyectado
    single { AlertsRepository(get()) }
}
