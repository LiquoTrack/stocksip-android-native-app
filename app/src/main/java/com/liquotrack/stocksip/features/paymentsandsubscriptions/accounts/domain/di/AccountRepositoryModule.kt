package com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.domain.di

import com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.data.repositories.AccountRepositoryImpl
import com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.domain.repositories.AccountRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface AccountRepositoryModule {


    @Binds
    fun provideAccountRepository(impl: AccountRepositoryImpl): AccountRepository
}