package com.liquotrack.stocksip.features.authentication.login.data.remote.services

import com.liquotrack.stocksip.features.authentication.login.data.remote.model.SignInRequestDto
import com.liquotrack.stocksip.features.authentication.login.data.remote.model.SignInResponseDto
import com.liquotrack.stocksip.features.authentication.login.data.remote.model.SignUpRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit service interface for authentication-related API calls.
 * Defines endpoints for user login and registration.
 */
interface AuthService {

    /**
     * Logs in a user with the provided credentials.
     *
     * @param request The sign-in request containing user credentials.
     * @return A response containing the sign-in result.
     */
    @POST("sign-in")
    suspend fun login(@Body request: SignInRequestDto): Response<SignInResponseDto>

    /**
     * Registers a new user with the provided details.
     *
     * @param request The sign-up request containing user details.
     * @return A response containing the sign-up result.
     */
    @POST("sign-up")
    suspend fun register(@Body request: SignUpRequestDto): Response<SignInResponseDto>
}