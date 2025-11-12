package com.liquotrack.stocksip.shared.di

import com.liquotrack.stocksip.BuildConfig
import com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.services.UserService
import com.liquotrack.stocksip.features.authentication.login.data.remote.services.AuthService
import com.liquotrack.stocksip.features.authentication.passwordrecover.data.remote.services.RecoverPasswordService
import com.liquotrack.stocksip.features.inventorymanagement.careguides.data.remote.services.CareGuideService
import com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.services.ProductService
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.services.WarehouseService
import com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.data.remote.services.AccountService
import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.data.remote.services.PlanService
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.services.SubscriptionService
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.services.CatalogService
import com.liquotrack.stocksip.features.profilemanagement.profile.data.remote.services.ProfileService
import com.liquotrack.stocksip.features.ordermanagement.data.remote.services.SalesOrderService
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

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    @Named("url")
    fun provideApiBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("url") baseUrl: String,
        okHttpClient: OkHttpClient,
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
    fun provideCareGuideService(retrofit: Retrofit): CareGuideService {
        return retrofit.create(CareGuideService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductService(retrofit: Retrofit): ProductService {
        return retrofit.create(ProductService::class.java)
    }

    @Provides
    @Singleton
    fun providePlanService(retrofit: Retrofit): PlanService {
        return retrofit.create(PlanService::class.java)
    }

    @Provides
    @Singleton
    fun provideSubscriptionService(retrofit: Retrofit) : SubscriptionService {
        return retrofit.create(SubscriptionService::class.java)
    }

    @Provides
    @Singleton
    fun provideAccountService(retrofit: Retrofit) : AccountService {
        return retrofit.create(AccountService::class.java)
    }

    @Provides
    @Singleton
    fun provideSalesOrderService(retrofit: Retrofit): SalesOrderService {
        return retrofit.create(SalesOrderService::class.java)
    }

    @Provides
    @Singleton
    fun provideRecoverPasswordService(retrofit: Retrofit): RecoverPasswordService {
        return retrofit.create(RecoverPasswordService::class.java)
    }

    @Provides
    @Singleton
    fun provideCatalogService(retrofit: Retrofit): CatalogService {
        return retrofit.create(CatalogService::class.java)
    }
}