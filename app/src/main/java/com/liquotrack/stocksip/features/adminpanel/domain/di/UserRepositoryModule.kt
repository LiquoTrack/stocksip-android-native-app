package com.liquotrack.stocksip.features.adminpanel.domain.di

import com.liquotrack.stocksip.features.adminpanel.data.repositories.UserRepositoryImpl
import com.liquotrack.stocksip.features.adminpanel.domain.repositories.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AdminRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAdminRepository(
        repositoryImpl: UserRepositoryImpl
    ): UserRepository
}