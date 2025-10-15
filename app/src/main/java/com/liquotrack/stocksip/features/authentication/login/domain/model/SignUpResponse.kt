package com.liquotrack.stocksip.features.authentication.login.domain.model

/**
 * Data class representing the response received after a user signs up.
 *
 * @param message A message indicating the result of the sign-up operation.
 */
data class SignUpResponse(
    val message: String
)
