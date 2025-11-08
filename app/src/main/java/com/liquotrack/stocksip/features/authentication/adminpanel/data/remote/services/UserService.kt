package com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.services

import com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models.DeleteUserRequest
import com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models.RegisterSubUserDto
import com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models.SubUserWrapperDto
import com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models.UserDto

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
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
}