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

    /**
     * Retrieves the role of the account with the given ID.
     * @param accountId The ID of the account.
     * @return The role string for the account.
     */
    suspend fun getAccountRole(accountId: String): String

    /**
     * Retrieves the email and phone of the account with the given ID.
     * @return Pair(email, phone). Nulls when not available.
     */
    suspend fun getAccountContacts(accountId: String): Pair<String?, String?>
}