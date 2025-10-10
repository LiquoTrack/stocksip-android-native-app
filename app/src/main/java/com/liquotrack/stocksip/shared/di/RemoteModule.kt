package com.liquotrack.stocksip.shared.di

import com.liquotrack.stocksip.features.adminpanel.data.remote.services.UserService
import com.liquotrack.stocksip.features.authentication.login.data.remote.services.AuthService
import com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.services.ProductService
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.services.WarehouseService
import com.liquotrack.stocksip.features.profilemanagement.profile.data.remote.services.ProfileService
import com.liquotrack.stocksip.shared.data.local.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Module to provide remote dependencies like Retrofit instance.
 * Configures Retrofit with the base API URL and Gson converter.
 */
@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    /**
     * Provides the base URL for the API.
     * Change this URL to point to the desired API endpoint.
     */
    @Provides
    @Singleton
    @Named("url")
    fun provideApiBaseUrl(): String {
        return "http://10.0.2.2:5283/api/v1/"
    }

    /**
     * Provides a singleton Retrofit instance configured with the base URL and Gson converter.
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("url") baseUrl: String,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideWarehouseService(retrofit: Retrofit) : WarehouseService {
        return retrofit.create(WarehouseService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileApiService(retrofit: Retrofit): ProfileService {
        return retrofit.create(ProfileService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductService(retrofit: Retrofit): ProductService {
        return retrofit.create(ProductService::class.java)
    }
}