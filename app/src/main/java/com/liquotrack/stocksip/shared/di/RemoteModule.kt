package com.liquotrack.stocksip.shared.di

import com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.services.WarehouseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Module to provide remote dependencies like Retrofit instance.
 * Configures Retrofit with the base API URL and Gson converter.
 */
@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    /**
     * Provides the base URL for the API.
     * Change this URL to point to the desired API endpoint.
     */
    @Provides
    @Singleton
    @Named("url")
    fun provideApiBaseUrl(): String {
        return "http://10.0.2.2:5283/api/v1/"
    }

    /**
     * Provides a singleton Retrofit instance configured with the base URL and Gson converter.
     */
    @Provides
    @Singleton
    fun provideRetrofit(@Named("url") baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     *
     */
    @Provides
    @Singleton
    fun provideWarehouseService(retrofit: Retrofit) : WarehouseService {
        return retrofit.create(WarehouseService::class.java)
    }
}