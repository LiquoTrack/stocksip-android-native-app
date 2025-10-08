package com.liquotrack.stocksip.features.authentication.login.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the sign-in request information for a user.
 *
 * @param email The user's email address.
 * @param password The user's password.
 */
data class SignInRequestDto(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)
