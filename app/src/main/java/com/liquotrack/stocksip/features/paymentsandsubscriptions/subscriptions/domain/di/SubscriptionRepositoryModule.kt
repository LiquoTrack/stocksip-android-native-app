package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.di

import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.repositories.SubscriptionRepositoryImpl
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.repositories.SubscriptionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface SubscriptionRepositoryModule {

    @Binds
    fun provideSubscriptionRepository(impl: SubscriptionRepositoryImpl) : SubscriptionRepository
}