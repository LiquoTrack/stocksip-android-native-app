package com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.domain.repositories

/**
 * Repository interface for managing account-related operations.
 */
interface AccountRepository {

    /**
     * Retrieves the status of the account with the given ID.
     * @param accountId The ID of the account.
     * @return A string representing the account status.
     */
    suspend fun getAccountStatus(accountId: String): String
}