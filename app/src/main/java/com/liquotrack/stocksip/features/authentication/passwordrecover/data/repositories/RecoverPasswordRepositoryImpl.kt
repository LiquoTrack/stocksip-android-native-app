package com.liquotrack.stocksip.features.authentication.passwordrecover.data.repositories

import com.liquotrack.stocksip.features.authentication.passwordrecover.data.remote.models.RecoveryCodeRequestDto
import com.liquotrack.stocksip.features.authentication.passwordrecover.data.remote.models.UpdatePasswordRequestDto
import com.liquotrack.stocksip.features.authentication.passwordrecover.data.remote.models.VerifyCodeRequestDto
import com.liquotrack.stocksip.features.authentication.passwordrecover.data.remote.services.RecoverPasswordService
import com.liquotrack.stocksip.features.authentication.passwordrecover.domain.repositories.RecoverPasswordRepository
import javax.inject.Inject

/**
 * Implementation of [RecoverPasswordRepository] that uses [RecoverPasswordService]
 * to perform password recovery operations.
 */
class RecoverPasswordRepositoryImpl @Inject constructor(
    private val apiService: RecoverPasswordService
) : RecoverPasswordRepository {

    /**
     * Sends a recovery code to the specified email.
     *
     * @param email The email address to send the recovery code to.
     * @return A message indicating the result of the operation.
     * @throws Exception if the operation fails.
     */
    override suspend fun sendRecoveryCode(email: String): String {

        try {
            val response = apiService.sendRecoveryCode(RecoveryCodeRequestDto(email))
            if (response.isSuccessful) {
                val body = response.body()
                return body?.message ?: "Recovery code sent successfully"
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error sending recovery code"
                throw Exception(errorMsg)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * Verifies the recovery code for the specified email.
     *
     * @param code The recovery code to verify.
     * @param email The email address associated with the recovery code.
     * @return A message indicating the result of the operation.
     * @throws Exception if the operation fails.
     */

    override suspend fun verifyRecoveryCode(
        code: String,
        email: String
    ): String {

        try {
            val response = apiService.verifyRecoveryCode(VerifyCodeRequestDto(code, email))

            if (response.isSuccessful) {
                val body = response.body()
                return body?.message ?: "Code verified successfully"
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error verifying code"
                throw Exception(errorMsg)
            }
        } catch (e: Exception) {
            throw e
        }
    }


    /**
     * Resets the password for the specified email.
     *
     * @param email The email address associated with the account.
     * @param newPassword The new password to set.
     * @return A message indicating the result of the operation.
     * @throws Exception if the operation fails.
     */
    override suspend fun resetPassword(
        email: String,
        newPassword: String
    ): String {
        try {
            val response = apiService.resetPassword(UpdatePasswordRequestDto(email , newPassword))
            if (response.isSuccessful) {
                val body = response.body()
                return body?.message ?: "Password reset successfully"
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error resetting password"
                throw Exception(errorMsg)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}