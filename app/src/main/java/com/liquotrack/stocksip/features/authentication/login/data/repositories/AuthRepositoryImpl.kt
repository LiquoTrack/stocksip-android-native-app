package com.liquotrack.stocksip.features.authentication.login.data.repositories

import com.liquotrack.stocksip.common.utils.Resource
import com.liquotrack.stocksip.features.authentication.login.data.remote.model.SignInRequestDto
import com.liquotrack.stocksip.features.authentication.login.data.remote.model.SignUpRequestDto
import com.liquotrack.stocksip.features.authentication.login.data.remote.services.AuthService
import com.liquotrack.stocksip.features.authentication.login.domain.model.User
import com.liquotrack.stocksip.features.authentication.login.domain.repositories.AuthRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val service: AuthService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Resource<User> =
        withContext(Dispatchers.IO) {
            try {
                val response = service.login(SignInRequestDto(email = email, password = password))

                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        val user = User(
                            userId = loginResponse.userId,
                            email = loginResponse.email,
                            username = loginResponse.userName,
                            token = loginResponse.token
                        )
                        tokenManager.saveToken(loginResponse.token)
                        return@withContext Resource.Success(data = user)
                    }
                }

                return@withContext Resource.Error("Unknown error")
            } catch (e: Exception) {
                return@withContext Resource.Error(e.localizedMessage ?: "Connection error")
            }
        }

    override suspend fun register(
        email: String,
        username: String,
        password: String,
        businessName: String,
        role: String
    ): Resource<User> = withContext(Dispatchers.IO) {
        try {
            val request = SignUpRequestDto(
                email = email,
                username = username,
                password = password,
                businessName = businessName,
                role = role
            )
            val response = service.register(request)

            if (response.isSuccessful) {
                response.body()?.let { registerResponse ->
                    val user = User(
                        userId = registerResponse.userId,
                        email = registerResponse.email,
                        username = registerResponse.userName,
                        token = registerResponse.token,
                    )
                    tokenManager.saveToken(registerResponse.token)
                    return@withContext Resource.Success(data = user)
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
                tokenManager.clearTokens()
                return@withContext Resource.Success(Unit)
            } catch (e: Exception) {
                return@withContext Resource.Error(e.localizedMessage ?: "Error logging out")
            }
        }
}