package com.liquotrack.stocksip.features.adminpanel.domain.repositories

import com.liquotrack.stocksip.shared.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsers(): Flow<List<User>>
    suspend fun createUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(userId: String)
}