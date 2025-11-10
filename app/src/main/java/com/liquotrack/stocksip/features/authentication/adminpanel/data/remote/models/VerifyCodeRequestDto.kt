package com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models


import com.google.gson.annotations.SerializedName

/**
 * Data transfer object for verifying a code sent to a user's email.
 *
 * @param code The verification code.
 * @param email The email of the user to verify.
 */
data class VerifyCodeRequestDto(
    @SerializedName("code")
    val code: String,
    @SerializedName("email")
    val email: String
)