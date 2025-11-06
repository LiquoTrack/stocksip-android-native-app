package com.liquotrack.stocksip.features.authentication.adminpanel.data.repositories

import com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.services.UserService
import com.liquotrack.stocksip.features.authentication.adminpanel.domain.domain.AccountUsers
import com.liquotrack.stocksip.features.authentication.adminpanel.domain.domain.SubUser
import com.liquotrack.stocksip.features.authentication.adminpanel.domain.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

/**
 * Implementation of the UserRepository interface for managing user-related operations.
 *
 * @property apiService The UserService instance for making API calls.
 */
class UserRepositoryImpl @Inject constructor(
    private val apiService: UserService
) : UserRepository {

    /**
     * Retrieves all sub-users associated with a specific account and role.
     *
     * @param accountId The ID of the account.
     * @param role The role of the users to retrieve.
     * @return A Response containing a list of AccountUsers.
     */
    override suspend fun getAllSubUsers(accountId: String, role: String): Response<List<AccountUsers>> = withContext(Dispatchers.IO) {


        val response = apiService.getAllSubUsers(accountId, role)

        if (response.isSuccessful) {
            val users = response.body()?.map { userWrapperDto ->
                AccountUsers(
                    maxUsersAllowed = userWrapperDto.maxUsersAllowed,
                    totalUsers = userWrapperDto.totalUsers,
                    users = userWrapperDto.users.map { subUserDto ->
                        SubUser(
                            id = subUserDto.userId,
                            email = subUserDto.email,
                            userRole = subUserDto.role,
                            profileId = subUserDto.profileId,
                            fullName = subUserDto.fullName,
                            phoneNumber = subUserDto.phoneNumber,
                            profilePictureUrl = subUserDto.profilePictureUrl,
                            profileRole = subUserDto.profileRole
                        )
                    }
                )
            } ?: emptyList()

            Response.success(users)
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun createSubUser(user: SubUser) : Response<SubUser> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(userId: String) {
        TODO("Not yet implemented")
    }

}