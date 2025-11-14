package com.liquotrack.stocksip.features.alerts.domain.di

import com.liquotrack.stocksip.features.alerts.data.remote.services.AlertsApiService
import com.liquotrack.stocksip.features.alerts.data.repositories.AlertsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlertsModule {

    @Provides
    @Singleton
    fun provideAlertsApiService(retrofit: Retrofit): AlertsApiService {
        return retrofit.create(AlertsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAlertsRepository(api: AlertsApiService): AlertsRepository {
        return AlertsRepository(api)
    }
}