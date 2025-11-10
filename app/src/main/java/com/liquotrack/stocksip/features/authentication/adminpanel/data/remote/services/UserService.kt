package com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.services

import com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models.DeleteUserRequest
import com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models.RegisterSubUserDto
import com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models.SubUserWrapperDto
import com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models.UpdatePasswordRequestDto
import com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models.UserDto
import com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models.VerifyCodeRequestDto

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Service to manage users within an account.
 */
interface UserService {

    /**
     * Retrieves a list of users associated with a specific account and role.
     *
     * @param accountId The ID of the account.
     * @param role The role of the users to retrieve.
     * @return A list of SubUserWrapperDto representing the users.
     */
    @GET("accounts/{accountId}/users")
    suspend fun getAllSubUsers(
        @Path("accountId") accountId: String,
        @Query("role") role: String
    ): Response<SubUserWrapperDto>

    /**
     * Registers a new sub-user under a specific account.
     *
     * @param accountId The ID of the account.
     * @param subUser The details of the sub-user to register.
     * @return A list of UserDto representing the registered users.
     */
    @POST("accounts/{accountId}/users")
    suspend fun registerSubUser(
        @Path("accountId") accountId: String,
        @Body subUser: RegisterSubUserDto) : Response<UserDto>

    /**
     * Deletes a user by their user ID.
     *
     * @param userId The ID of the user to delete.
     * @return The UserDto of the deleted user.
     */
    @HTTP(method = "DELETE", path = "users/{userId}", hasBody = true)
    suspend fun deleteUser(
        @Path("userId") userId: String,
        @Body request: DeleteUserRequest
    ): Response<Unit>

    /**
     * Sends a recovery code to the specified user.
     *
     * @param userId The ID of the user to send the recovery code to.
     * @return A Response indicating the success or failure of the operation.
     */
    @POST("users/{userId}/recovery-code")
    suspend fun sendRecoveryCode(@Path("userId") userId: String): Response<Unit>

    /**
     * Verifies the recovery code for the specified user.
     *
     * @param userId The ID of the user whose code is to be verified.
     * @param verifyCodeRequestDto The verification code details.
     * @return A Response indicating the success or failure of the operation.
     */
    @POST("users/{userId}/verify-code")
    suspend fun verifyRecoveryCode(
        @Path("userId") userId: String,
        @Body verifyCodeRequestDto: VerifyCodeRequestDto)
    : Response<Unit>

    /**
     * Resets the password for the specified user.
     *
     * @param userId The ID of the user whose password is to be reset.
     * @param newPassword The new password details.
     * @return A Response indicating the success or failure of the operation.
     */
    @PUT("users/{userId}/reset-password")
    suspend fun resetPassword(
        @Path("userId") userId: String,
        @Body newPassword: UpdatePasswordRequestDto
    ): Response<Unit>
}