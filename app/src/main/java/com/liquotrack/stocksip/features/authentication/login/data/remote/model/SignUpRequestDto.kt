package com.liquotrack.stocksip.features.authentication.login.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the sign-up information for a user.
 *
 * @param email The user's email address.
 * @param password The user's password.
 * @param username The username.
 * @param businessName The name of the user's business.
 * @param role The role of the user (e.g., "Liquor Store Owner" or "Supplier").
 */
data class SignUpRequestDto(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("name")
    val username: String,

    @SerializedName("businessName")
    val businessName: String,

    @SerializedName("role")
    val role: String
)