package com.liquotrack.stocksip.features.authentication.passwordrecover.domain.di

import com.liquotrack.stocksip.features.authentication.passwordrecover.data.repositories.RecoverPasswordRepositoryImpl
import com.liquotrack.stocksip.features.authentication.passwordrecover.domain.repositories.RecoverPasswordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RecoverPasswordRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRecoverPasswordRepository(impl: RecoverPasswordRepositoryImpl) : RecoverPasswordRepository
}