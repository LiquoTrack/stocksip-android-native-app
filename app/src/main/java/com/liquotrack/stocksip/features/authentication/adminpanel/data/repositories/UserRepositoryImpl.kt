package com.liquotrack.stocksip.features.authentication.adminpanel.data.repositories

import android.util.Log
import com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models.DeleteUserRequest
import com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models.RegisterSubUserDto
import com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.services.UserService
import com.liquotrack.stocksip.features.authentication.adminpanel.domain.domain.AccountUsers
import com.liquotrack.stocksip.features.authentication.adminpanel.domain.domain.SubUser
import com.liquotrack.stocksip.features.authentication.adminpanel.domain.repositories.UserRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

/**
 * Implementation of the UserRepository interface for managing user-related operations.
 *
 * @property apiService The UserService instance for making API calls.
 */
class UserRepositoryImpl @Inject constructor(
    private val apiService: UserService,
    private val tokenManager: TokenManager
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

    override suspend fun createSubUser(user: SubUser) : Response<SubUser> = withContext(Dispatchers.IO) {
        try {
            val accountId = tokenManager.getAccountId()
                ?: return@withContext Response.error(400, okhttp3.ResponseBody.create(null, "Missing accountId"))

            val request = RegisterSubUserDto(
                email = user.email,
                name = user.fullName,
                password = "ChangeMe123!",
                phoneNumber = user.phoneNumber,
                profileRole = user.profileRole,
                role = user.userRole
            )

            val response = apiService.registerSubUser(accountId, request)
            Log.d(
                "UserRepositoryImpl",
                "createSubUser -> code=${'$'}{response.code()} success=${'$'}{response.isSuccessful}"
            )

            if (response.isSuccessful) {
                val dto = response.body()
                val created = dto?.let {
                    SubUser(
                        id = it.userId,
                        email = it.email,
                        userRole = it.role,
                        profileId = it.profileId,
                        fullName = it.fullName,
                        phoneNumber = it.phoneNumber,
                        profilePictureUrl = it.profilePictureUrl,
                        profileRole = it.profileRole
                    )
                }
                return@withContext Response.success(created)
            } else {
                val errorBodyString = response.errorBody()?.string()
                Log.e(
                    "UserRepositoryImpl",
                    "createSubUser failed -> code=${'$'}{response.code()} body=${errorBodyString ?: "<empty>"}"
                )
                return@withContext Response.error(
                    response.code(),
                    errorBodyString?.let { ResponseBody.create(null, it) } ?: ResponseBody.create(null, "")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
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