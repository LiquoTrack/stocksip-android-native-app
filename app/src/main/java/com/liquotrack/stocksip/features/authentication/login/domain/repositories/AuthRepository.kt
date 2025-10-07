package com.liquotrack.stocksip.features.authentication.login.domain.repositories

import com.liquotrack.stocksip.common.utils.Resource
import com.liquotrack.stocksip.features.authentication.login.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Resource<User>
    suspend fun logout(): Resource<Unit>
    suspend fun forgotPassword(email: String): Resource<Unit>
}