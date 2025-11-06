package com.liquotrack.stocksip.features.authentication.adminpanel.domain.repositories

import com.liquotrack.stocksip.shared.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing user-related operations in the admin panel.
 */
interface UserRepository {

    /**
     * Retrieves a flow of all sub-users.
     *
     * @return A flow emitting a list of all sub-users.
     */
    fun getAllSubUsersUsers(): Flow<List<User>>

    /**
     * Creates a new sub-user.
     *
     * @param user The user object containing details of the sub-user to be created.
     */
    suspend fun createSubUser(user: User)

    /**
     * Updates an existing sub-user.
     *
     * @param userId The ID of the user to be updated.
     */
    suspend fun deleteUser(userId: String)
}