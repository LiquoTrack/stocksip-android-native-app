package com.liquotrack.stocksip.features.authentication.login.data.remote.services

import com.liquotrack.stocksip.features.authentication.login.data.remote.model.SignInRequestDto
import com.liquotrack.stocksip.features.authentication.login.data.remote.model.SignInResponseDto
import com.liquotrack.stocksip.features.authentication.login.data.remote.model.SignUpRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/v1/authentication/sign-in")
    suspend fun login(@Body request: SignInRequestDto): Response<SignInResponseDto>

    @POST("api/v1/authentication/sign-out")
    suspend fun logout(): Response<Unit>

    @POST("api/v1/authentication/forgot-password")
    suspend fun forgotPassword(@Body email: String): Response<Unit>

    @POST("api/v1/authentication/sign-up")
    suspend fun register(@Body request: SignUpRequestDto): Response<SignInResponseDto>
}