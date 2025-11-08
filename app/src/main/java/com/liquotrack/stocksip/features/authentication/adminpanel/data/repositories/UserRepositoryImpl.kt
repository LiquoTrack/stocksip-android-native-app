package com.liquotrack.stocksip.features.authentication.adminpanel.data.repositories

import com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models.DeleteUserRequest
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
    override suspend fun getAllSubUsers(accountId: String, role: String): Response<AccountUsers> =
        withContext(Dispatchers.IO) {

            try {
                val response = apiService.getAllSubUsers(accountId, role)

                if (response.isSuccessful) {
                    val userWrapperDto = response.body()

                    val accountUsers = userWrapperDto?.let {
                        AccountUsers(
                            maxUsersAllowed = it.maxUsersAllowed,
                            totalUsers = it.totalUsers,
                            users = it.users.map { subUserDto ->
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
                    }

                    Response.success(accountUsers)
                } else {
                    response.errorBody()?.string()
                    Response.error(response.code(), response.errorBody()!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }

    override suspend fun createSubUser(user: SubUser) : Response<SubUser> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(userId: String, profileId: String) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteUser(
                userId = userId,
                request = DeleteUserRequest(profileId = profileId)
            )

            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                throw Exception(errorBody ?: "Failed to delete user")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}