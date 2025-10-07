package com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.di

import com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.repositories.WarehouseRepositoryImpl
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.repositories.WarehouseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Hilt module to bind the WarehouseRepository interface to its implementation.
 * This allows for dependency injection of the repository in ViewModel components.
 */
@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    /**
     * Binds the WarehouseRepository interface to the WarehouseRepositoryImpl implementation.
     *
     * @param impl The implementation of WarehouseRepository.
     * @return The bound WarehouseRepository instance.
     */
    @Binds
    fun provideWarehouseRepository(impl: WarehouseRepositoryImpl): WarehouseRepository
}