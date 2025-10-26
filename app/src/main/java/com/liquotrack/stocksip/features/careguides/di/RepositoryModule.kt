package com.liquotrack.stocksip.features.careguides.di

import com.liquotrack.stocksip.features.careguides.data.repositories.CareGuideRepositoryImpl
import com.liquotrack.stocksip.features.careguides.domain.CareGuideRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Hilt module to bind the CareGuideRepository interface to its implementation.
 * This allows for dependency injection of the repository in ViewModel components.
 */
@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {
    /**
     * Binds the CareGuideRepository interface to the CareGuideRepositoryImpl implementation.
     *
     * @param impl The implementation of CareGuideRepository.
     * @return The bound CareGuideRepository instance.
     */
    @Binds
    fun provideCareGuideRepository(impl: CareGuideRepositoryImpl): CareGuideRepository
}