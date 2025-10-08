package com.liquotrack.stocksip.features.authentication.login.data.repositories

import android.content.SharedPreferences
import com.liquotrack.stocksip.common.utils.Resource
import com.liquotrack.stocksip.features.authentication.login.data.remote.model.SignInRequestDto
import com.liquotrack.stocksip.features.authentication.login.data.remote.model.SignUpRequestDto
import com.liquotrack.stocksip.features.authentication.login.data.remote.services.AuthService
import com.liquotrack.stocksip.shared.domain.model.User
import com.liquotrack.stocksip.features.authentication.login.domain.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import androidx.core.content.edit

class AuthRepositoryImpl @Inject constructor(
    private val service: AuthService,
    private val sharedPreferences: SharedPreferences
) : AuthRepository {

    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    override suspend fun login(email: String, password: String): Resource<User> =
        withContext(Dispatchers.IO) {
            try {
                val response = service.login(SignInRequestDto(email, password))

                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        val user = User(
                            email = loginResponse.user.email,
                            username = loginResponse.user.username,
                            userRole = loginResponse.user.userRole,
                            accountId = loginResponse.user.accountId
                        )

                        sharedPreferences.edit {
                            putString(KEY_TOKEN, loginResponse.token)
                        }

                        return@withContext Resource.Success(user)
                    }
                }
                return@withContext Resource.Error("Login failed")
            } catch (e: Exception) {
                return@withContext Resource.Error(e.localizedMessage ?: "Connection error")
            }
        }

    override suspend fun register(
        email: String,
        username: String,
        password: String,
        userRole: String
    ): Resource<User> = withContext(Dispatchers.IO) {
        try {
            val request = SignUpRequestDto(email, username, password, userRole)
            val response = service.register(request)

            if (response.isSuccessful) {
                response.body()?.let { registerResponse ->
                    val user = User(
                        email = registerResponse.user.email,
                        username = registerResponse.user.username,
                        userRole = registerResponse.user.userRole,
                        accountId = registerResponse.user.accountId
                    )

                    sharedPreferences.edit {
                        putString(KEY_TOKEN, registerResponse.token)
                    }

                    return@withContext Resource.Success(user)
                }
            }
            return@withContext Resource.Error("Registration failed")
        } catch (e: Exception) {
            return@withContext Resource.Error(e.localizedMessage ?: "Connection error")
        }
    }

    override suspend fun logout(): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                sharedPreferences.edit {
                    remove(KEY_TOKEN)
                        .remove(KEY_REFRESH_TOKEN)
                }

                return@withContext Resource.Success(Unit)
            } catch (e: Exception) {
                return@withContext Resource.Error(e.localizedMessage ?: "Error logging out")
            }
        }
}