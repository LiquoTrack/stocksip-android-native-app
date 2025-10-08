package com.liquotrack.stocksip.features.profilemanagement.profile.domain.di

import com.liquotrack.stocksip.features.profilemanagement.profile.data.repositories.ProfileRepositoryImpl
import com.liquotrack.stocksip.features.profilemanagement.profile.domain.repositories.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        repositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository
}