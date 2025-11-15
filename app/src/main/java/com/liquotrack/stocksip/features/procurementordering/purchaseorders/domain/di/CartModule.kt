package com.liquotrack.stocksip.features.procurementordering.purchaseorders.domain.di

import android.content.Context
import com.liquotrack.stocksip.features.procurementordering.purchaseorders.data.local.CartDao
import com.liquotrack.stocksip.features.procurementordering.purchaseorders.data.local.CartDatabase
import com.liquotrack.stocksip.features.procurementordering.purchaseorders.domain.repositories.CartRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CartModule {

    @Provides
    @Singleton
    fun provideCartDatabase(@ApplicationContext context: Context): CartDatabase {
        return CartDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideCartDao(database: CartDatabase): CartDao {
        return database.cartDao()
    }

    @Provides
    @Singleton
    fun provideCartRepository(cartDao: CartDao): CartRepository {
        return CartRepository(cartDao)
    }
}