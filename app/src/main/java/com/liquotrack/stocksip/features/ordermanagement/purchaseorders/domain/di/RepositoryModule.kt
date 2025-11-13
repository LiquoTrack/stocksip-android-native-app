package com.liquotrack.stocksip.features.ordermanagement.purchaseorders.domain.di

import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.repositories.PurchaseOrderRepositoryImpl
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.domain.repositories.PurchaseOrderRepository
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
    abstract fun bindPurchaseOrderRepository(
        impl: PurchaseOrderRepositoryImpl
    ): PurchaseOrderRepository
}