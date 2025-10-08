package com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.di

import com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.services.WarehouseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    fun provideWarehouseService(retrofit: Retrofit) : WarehouseService {
        return retrofit.create(WarehouseService::class.java)
    }
}