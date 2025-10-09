package com.liquotrack.stocksip.features.adminpanel.data.remote.services

import com.liquotrack.stocksip.shared.domain.model.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @GET("api/v1/users")
    suspend fun getAllUsers(): GetUsersResponse

    @POST("api/v1/users")
    suspend fun createUser(@Body user: User)

    @PUT("api/v1/users")
    suspend fun updateUser(@Body user: User)

    @DELETE("api/v1/users/{id}")
    suspend fun deleteUser(@Path("id") userId: String)
}

data class GetUsersResponse(
    val users: List<User>
)