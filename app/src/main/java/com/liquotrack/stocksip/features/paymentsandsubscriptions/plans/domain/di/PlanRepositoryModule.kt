package com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.domain.di

import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.data.repositories.PlanRepositoryImpl
import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.domain.repositories.PlanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface PlanRepositoryModule {

    @Binds
    fun providePlanRepository(planRepositoryImpl: PlanRepositoryImpl): PlanRepository
}