package com.liquotrack.stocksip.features.adminpanel.data.repositories

import com.liquotrack.stocksip.features.adminpanel.data.remote.services.UserService
import com.liquotrack.stocksip.features.adminpanel.domain.repositories.UserRepository
import com.liquotrack.stocksip.shared.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: UserService
) : UserRepository {

    override fun getAllUsers(): Flow<List<User>> = flow {
        try {
            val response = apiService.getAllUsers()
            emit(response.users)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun createUser(user: User) {
        apiService.createUser(user)
    }

    override suspend fun updateUser(user: User) {
        apiService.updateUser(user)
    }

    override suspend fun deleteUser(userId: String) {
        apiService.deleteUser(userId)
    }
}