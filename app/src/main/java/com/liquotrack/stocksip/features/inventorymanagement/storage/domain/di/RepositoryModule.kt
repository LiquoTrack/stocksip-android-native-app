package com.liquotrack.stocksip.features.inventorymanagement.storage.domain.di

import com.liquotrack.stocksip.features.inventorymanagement.storage.data.repositories.ProductRepositoryImpl
import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.repositories.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Dagger Hilt module for providing repository implementations.
 *
 * This module binds the ProductRepository interface to its implementation,
 * ProductRepositoryImpl, allowing for dependency injection throughout the app.
 */
@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    /**
     * Binds the ProductRepository implementation to its interface.
     *
     * @param impl The ProductRepositoryImpl instance.
     * @return The ProductRepository interface.
     */
    @Binds
    fun provideProductRepository(impl: ProductRepositoryImpl): ProductRepository
}