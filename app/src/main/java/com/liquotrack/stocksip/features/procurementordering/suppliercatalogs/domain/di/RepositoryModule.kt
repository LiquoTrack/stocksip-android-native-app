package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.di

import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.repositories.CatalogRepositoryImpl
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.repositories.CatalogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCatalogRepository(
        impl: CatalogRepositoryImpl
    ): CatalogRepository
}