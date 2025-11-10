package com.liquotrack.stocksip.features.authentication.passwordrecover.data.remote.services

import com.liquotrack.stocksip.features.authentication.passwordrecover.data.remote.models.ConfirmationCodeResponseDto
import com.liquotrack.stocksip.features.authentication.passwordrecover.data.remote.models.RecoveryCodeRequestDto
import com.liquotrack.stocksip.features.authentication.passwordrecover.data.remote.models.UpdatePasswordRequestDto
import com.liquotrack.stocksip.features.authentication.passwordrecover.data.remote.models.VerifyCodeRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Service for handling password recovery operations.
 */
interface RecoverPasswordService {

    /**
     * Sends a recovery code to the specified user.
     *
     * @param request The recovery code request details.
     * @return A Response containing a message about the operation's success or failure.
     */
    @POST("users/recovery-code")
    suspend fun sendRecoveryCode(
        @Body request: RecoveryCodeRequestDto
    ): Response<ConfirmationCodeResponseDto>

    /**
     * Verifies the recovery code for the specified user.
     *
     * @param userId The ID of the user whose code is to be verified.
     * @param verifyCodeRequestDto The verification code details.
     * @return A Response indicating the success or failure of the operation.
     */
    @POST("users/verify-code")
    suspend fun verifyRecoveryCode(
        @Path("userId") userId: String,
        @Body verifyCodeRequestDto: VerifyCodeRequestDto)
            : Response<ConfirmationCodeResponseDto>

    /**
     * Resets the password for the specified user.
     *
     * @param userId The ID of the user whose password is to be reset.
     * @param newPassword The new password details.
     * @return A Response indicating the success or failure of the operation.
     */
    @PUT("users/reset-password")
    suspend fun resetPassword(
        @Path("userId") userId: String,
        @Body newPassword: UpdatePasswordRequestDto
    ): Response<ConfirmationCodeResponseDto>
}