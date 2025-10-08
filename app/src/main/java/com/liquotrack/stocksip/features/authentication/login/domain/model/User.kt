package com.liquotrack.stocksip.features.authentication.login.domain.model

/**
 * Data class representing a user in the authentication system.
 *
 * @property userId The unique identifier for the user.
 * @property email The user's email address.
 * @property username The user's display name.
 * @property businessName The name of the user's business.
 * @property userRole The role of the user (e.g., "Liquor Store Owner" or "Supplier").
 */
data class User(
    val userId: String,
    val email: String,
    val username: String,
    val token: String,
)