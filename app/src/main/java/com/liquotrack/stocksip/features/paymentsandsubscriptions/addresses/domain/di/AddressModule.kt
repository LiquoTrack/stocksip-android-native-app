package com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.domain.di

import android.content.Context
import androidx.room.Room
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.local.AddressDao
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.local.AddressDatabase
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.remote.services.AddressService
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.repositories.AddressRepositoryImpl
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.domain.repositories.AddressRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AddressModule {

    @Provides
    @Singleton
    fun provideAddressDatabase(
        @ApplicationContext context: Context
    ): AddressDatabase {
        return Room.databaseBuilder(
            context,
            AddressDatabase::class.java,
            AddressDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideAddressDao(database: AddressDatabase): AddressDao {
        return database.addressDao()
    }

    @Provides
    @Singleton
    fun provideAddressRepository(
        addressDao: AddressDao,
        addressService: AddressService
    ): AddressRepository {
        return AddressRepositoryImpl(addressDao, addressService)
    }
}