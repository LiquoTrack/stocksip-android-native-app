package com.liquotrack.stocksip.features.authentication.login.domain.model

/**
 * Data class representing a user in the authentication system.
 *
 * @property userId The unique identifier for the user.
 * @property email The user's email address.
 * @property username The user's display name.
 * @property token The authentication token for the user.
 * @property accountId The account identifier associated with the user.
 */
data class User(
    val userId: String,
    val email: String,
    val username: String,
    val token: String,
    val accountId: String
)