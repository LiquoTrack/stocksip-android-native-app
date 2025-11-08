package com.liquotrack.stocksip.features.authentication.adminpanel.domain.repositories

import com.liquotrack.stocksip.features.authentication.adminpanel.domain.domain.AccountUsers
import com.liquotrack.stocksip.features.authentication.adminpanel.domain.domain.SubUser
import retrofit2.Response

/**
 * Repository interface for managing user-related operations in the admin panel.
 */
interface UserRepository {

    /**
     * Retrieves a flow of all sub-users.
     *
     * @return A flow emitting a list of all sub-users.
     */
    suspend fun getAllSubUsers(accountId: String, role: String): Response<AccountUsers>

    /**
     * Creates a new sub-user.
     *
     * @param user The user object containing details of the sub-user to be created.
     */
    suspend fun createSubUser(user: SubUser) : Response<SubUser>

    /**
     * Deletes an existing sub-user.
     *
     * @param userId The ID of the user to be deleted.
     * @param profileId The ID of the profile.
     */
    suspend fun deleteUser(userId: String, profileId: String)
}