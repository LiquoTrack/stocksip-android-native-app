package com.liquotrack.stocksip.features.ordermanagement.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import com.liquotrack.stocksip.features.ordermanagement.data.repositories.SalesOrderRepositoryImpl
import com.liquotrack.stocksip.features.ordermanagement.domain.SalesOrderRepository

/**
 * Hilt module to bind the SalesOrderRepository interface to its implementation.
 * This allows for dependency injection of the repository in ViewModel components.
 */
@Module
@InstallIn(ViewModelComponent::class)
interface SalesOrderRepositoryModule {
    /**
     * Binds the SalesOrderRepository interface to the SalesOrderRepositoryImpl implementation.
     *
     * @param impl The implementation of SalesOrderRepository.
     * @return The bound SalesOrderRepository instance.
     */
    @Binds
    fun bindSalesOrderRepository(salesOrderRepositoryImpl: SalesOrderRepositoryImpl): SalesOrderRepository
}