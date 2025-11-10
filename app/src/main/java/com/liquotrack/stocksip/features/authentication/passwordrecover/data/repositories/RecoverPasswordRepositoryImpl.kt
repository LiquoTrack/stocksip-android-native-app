package com.liquotrack.stocksip.features.authentication.passwordrecover.data.repositories

import com.liquotrack.stocksip.features.authentication.passwordrecover.data.remote.models.RecoveryCodeRequestDto
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

        val request = RecoveryCodeRequestDto(email = email)
        val response = apiService.sendRecoveryCode(request)
        if (response.isSuccessful) {
            val body = response.body()
            return body?.message ?: "Recovery code sent successfully"
        } else {
            val errorMsg = response.errorBody()?.string() ?: "Error sending recovery code"
            throw Exception(errorMsg)
        }
    }

    override suspend fun verifyRecoveryCode(
        code: String,
        email: String
    ): String {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(
        email: String,
        newPassword: String
    ): String {
        TODO("Not yet implemented")
    }
}