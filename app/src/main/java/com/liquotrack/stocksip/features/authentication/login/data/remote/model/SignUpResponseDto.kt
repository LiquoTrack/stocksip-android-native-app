package com.liquotrack.stocksip.features.authentication.login.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response received after a user signs up.
 *
 * @param message A message indicating the result of the sign-up operation.
 */
data class SignUpResponseDto(
    @SerializedName("message")
    val message: String
)
