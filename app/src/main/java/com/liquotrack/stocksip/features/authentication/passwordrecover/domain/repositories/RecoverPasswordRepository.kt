package com.liquotrack.stocksip.features.authentication.passwordrecover.domain.repositories

import retrofit2.Response

/**
 * Repository interface for handling password recovery operations.
 */
interface RecoverPasswordRepository {

    /**
     * Sends a recovery code to the specified email.
     *
     * @param email The email address to send the recovery code to.
     * @return A Result indicating success or failure.
     */
    suspend fun sendRecoveryCode(email: String): String

    /**
     * Verifies the recovery code for the specified email.
     *
     * @param code The recovery code to verify.
     * @param email The email address associated with the recovery code.
     * @return A Result indicating success or failure.
     */
    suspend fun verifyRecoveryCode(code: String, email: String): String

    /**
     * Resets the password for the specified email.
     *
     * @param email The email address associated with the account.
     * @param newPassword The new password to set.
     * @return A Result indicating success or failure.
     */
    suspend fun resetPassword(email: String, newPassword: String): String
}