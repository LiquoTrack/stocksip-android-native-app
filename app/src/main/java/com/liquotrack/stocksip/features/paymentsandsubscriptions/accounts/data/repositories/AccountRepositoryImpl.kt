package com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.data.repositories

import com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.data.remote.services.AccountService
import com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.domain.repositories.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AccountRepositoryImpl @Inject constructor(private val service: AccountService) : AccountRepository {

    override suspend fun getAccountStatus(accountId: String): String = withContext(Dispatchers.IO) {
        val response = service.getAccountStatus(accountId)
        if (response.isSuccessful) {
            response.body()?.let { statusResponse ->
                return@withContext statusResponse.accountStatus
            }
        }
        return@withContext "Unknown"
    }
}