package com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.data.remote.services

import com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.data.remote.models.AccountStatusDto
import com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.data.remote.models.AccountDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Service interface for managing account-related API calls.
 */
interface AccountService {

    /**
     *  Method to get the status of an account by its ID.
     */
    @GET("accounts/{accountId}/status")
    suspend fun getAccountStatus(@Path("accountId") accountId: String): Response<AccountStatusDto>

    /**
     * Method to get an account by its ID.
     */
    @GET("accounts/{accountId}")
    suspend fun getAccountById(@Path("accountId") accountId: String): Response<AccountDto>
}