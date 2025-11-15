package com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.di

import com.liquotrack.stocksip.features.inventorymanagement.inventories.data.repositories.InventoryRepositoryImpl
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.repositories.InventoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Hilt module to bind the InventoryRepository interface to its implementation.
 * This allows for dependency injection of the repository in ViewModel components.
 */
@Module
@InstallIn(ViewModelComponent::class)
interface InventoryRepositoryModule {

    /**
     * Binds the InventoryRepository interface to the InventoryRepositoryImpl implementation.
     *
     * @param impl The implementation of InventoryRepository.
     *
     * @return The bound InventoryRepository instance.
     */
    @Binds
    fun provideInventoryRepository(impl: InventoryRepositoryImpl): InventoryRepository
}