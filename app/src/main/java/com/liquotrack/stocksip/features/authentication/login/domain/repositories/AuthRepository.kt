package com.liquotrack.stocksip.features.authentication.login.domain.repositories

import com.liquotrack.stocksip.common.utils.Resource
import com.liquotrack.stocksip.features.authentication.login.domain.model.User

interface AuthRepository {
    /**
     * Authenticates user with email and password
     */
    suspend fun login(email: String, password: String): Resource<User>

    /**
     * Registers a new user with account information
     */
    suspend fun register(
        email: String,
        username: String,
        password: String,
        userRole: String
    ): Resource<User>

    /**
     * Logs out the current user
     */
    suspend fun logout(): Resource<Unit>

    /**
     * Sends password recovery email
     */
    suspend fun forgotPassword(email: String): Resource<Unit>
}