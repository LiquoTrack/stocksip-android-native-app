package com.liquotrack.stocksip.features.authentication.login.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response received after a successful sign-in.
 *
 * @param token The authentication token for the user session.
 * @param userId The unique identifier of the user.
 * @param email The email address of the user.
 * @param userName The username of the user.
 */
data class SignInResponseDto(

    @SerializedName("token")
    val token: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("username")
    val userName: String,
)
