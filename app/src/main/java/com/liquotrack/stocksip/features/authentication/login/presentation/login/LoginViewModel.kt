package com.liquotrack.stocksip.features.authentication.login.presentation.login

import android.content.ContentValues.TAG
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.common.utils.Resource
import com.liquotrack.stocksip.features.authentication.login.domain.repositories.AuthRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import com.liquotrack.stocksip.shared.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

/**
 * ViewModel for managing login and authentication state.
 *
 * @property repository Repository for authentication operations
 * @property tokenManager Manager for storing and retrieving authentication tokens
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _email = MutableStateFlow("")
    /**
     * Current email input value
     */
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    /**
     * Current password input value
     */
    val password: StateFlow<String> = _password

    private val _passwordVisible = MutableStateFlow(false)
    /**
     * Flag indicating whether the password should be visible in the UI
     */
    val passwordVisible: StateFlow<Boolean> = _passwordVisible

    private val _user = MutableStateFlow<User?>(null)
    /**
     * Currently authenticated user, null if no user is logged in
     */
    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow(false)
    /**
     * Flag indicating whether a login operation is in progress
     */
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    /**
     * Error message from login or authentication operations, null if no error
     */
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoggedOut = MutableStateFlow(false)
    /**
     * Flag indicating whether the user has successfully logged out.
     * This triggers navigation to the login screen.
     */
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut

    /**
     * Updates the email input value
     * @param value New email value
     */
    fun updateEmail(value: String) {
        _email.value = value
    }

    /**
     * Updates the password input value
     * @param value New password value
     */
    fun updatePassword(value: String) {
        _password.value = value
    }

    /**
     * Toggles the password visibility state
     */
    fun togglePasswordVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }

    /**
     * Performs user login with the current email and password
     */
    fun login() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val resource = repository.login(_email.value, _password.value)

            when (resource) {
                is Resource.Success -> {
                    val user = resource.data
                    user?.let {
                        tokenManager.saveToken(it.token)
                        tokenManager.saveAccountId(it.accountId)
                        _user.value = it
                    }
                }
                is Resource.Error -> {
                    _errorMessage.value = resource.message
                }
                is Resource.Loading -> {
                    // Loading state is already handled by _isLoading
                }
            }

            _isLoading.value = false
        }
    }

    /**
     * Logs out the current user and clears all authentication data
     * After calling this method, observers of isLoggedOut should navigate
     * the user to the login screen and call resetLogoutState().
     */
    fun logout() {
        viewModelScope.launch {
            // Clear all tokens and user data from SharedPreferences
            tokenManager.clearTokens()

            // Reset user state
            _user.value = null
            _email.value = ""
            _password.value = ""
            _errorMessage.value = null

            // Signal that logout is complete
            _isLoggedOut.value = true
        }
    }

    /**
     * Resets the logout state after navigation has been handled
     *
     * This should be called after the UI has responded to the logout event
     * and navigated to the login screen to prevent repeated navigation.
     */
    fun resetLogoutState() {
        _isLoggedOut.value = false
    }

    /**
     * Clears any current error message
     */
    fun clearError() {
        _errorMessage.value = null
    }

    fun setError(message: String) {
        _errorMessage.value = message
    }

    fun logGoogleIdTokenClaims(idToken: String) {
        try {
            val parts = idToken.split('.')
            if (parts.size < 2) {
                Log.w(TAG, "Formato de ID token inválido para análisis")
                return
            }

            val payloadBytes = Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP)
            val payloadJson = JSONObject(String(payloadBytes))
            val aud = payloadJson.optString("aud")
            val iss = payloadJson.optString("iss")
            val exp = payloadJson.optLong("exp")
            val now = System.currentTimeMillis() / 1000

            Log.d(
                TAG,
                "Google ID token claims -> aud=$aud, iss=$iss, exp=$exp, now=$now"
            )
        } catch (e: Exception) {
            Log.e(TAG, "No se pudieron decodificar las claims del ID token", e)
        }
    }

    /**
     * Saves the Google account session information
     *
     * This method is used when authenticating with Google Sign-In
     * to store the account ID for the current session.
     *
     * @param accountId The Google account ID to save
     */
    fun saveGoogleAccountSession(accountId: String) {
        if (accountId.isNotBlank()) {
            tokenManager.saveAccountId(accountId)
        }
    }

    /**
     * Authenticates with backend using Google idToken and persists session
     */
    fun authenticateWithGoogle(
        idToken: String,
        clientId: String,
        accessToken: String? = null,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val resource = repository.authenticateWithGoogle(
                idToken = idToken,
                clientId = clientId,
                accessToken = accessToken
            )

            when (resource) {
                is Resource.Success -> {
                    val user = resource.data
                    user?.let {
                        tokenManager.saveToken(it.token)
                        if (it.accountId.isNotBlank()) tokenManager.saveAccountId(it.accountId)
                        _user.value = it
                        onResult(true, null)
                    } ?: run {
                        onResult(false, "Invalid server response")
                    }
                }
                is Resource.Error -> {
                    _errorMessage.value = resource.message
                    onResult(false, resource.message)
                }
                is Resource.Loading -> { }
            }

            _isLoading.value = false
        }
    }
}