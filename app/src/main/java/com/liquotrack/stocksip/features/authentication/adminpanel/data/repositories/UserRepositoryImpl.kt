package com.liquotrack.stocksip.features.authentication.adminpanel.data.repositories

import com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.services.UserService
import com.liquotrack.stocksip.features.authentication.adminpanel.domain.repositories.UserRepository
import com.liquotrack.stocksip.shared.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: UserService
) : UserRepository {

    override fun getAllSubUsersUsers(): Flow<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun createSubUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(userId: String) {
        TODO("Not yet implemented")
    }

}