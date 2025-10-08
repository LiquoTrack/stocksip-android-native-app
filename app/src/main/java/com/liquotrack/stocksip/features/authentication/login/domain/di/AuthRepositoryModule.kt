package com.liquotrack.stocksip.features.authentication.login.domain.di

import com.liquotrack.stocksip.features.authentication.login.data.repositories.AuthRepositoryImpl
import com.liquotrack.stocksip.features.authentication.login.domain.repositories.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}