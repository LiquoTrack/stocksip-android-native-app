package com.liquotrack.stocksip.shared.data.local

import android.content.SharedPreferences
import javax.inject.Inject
import androidx.core.content.edit

/**
 * Manages the storage and retrieval of authentication tokens and account IDs using SharedPreferences.
 * This class provides methods to save, retrieve, and clear tokens and account IDs.
 */
class TokenManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    /**
     * Companion object to hold the keys used in SharedPreferences.
     * These keys are used to store and retrieve the authentication token,
     *
     * KEY_TOKEN: Key for storing the authentication token.
     * KEY_REFRESH_TOKEN: Key for storing the refresh token.
     * KEY_ACCOUNT_ID: Key for storing the account ID.
     */
    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_ACCOUNT_ID = "account_id"
    }

    /**
     * Saves the authentication token to SharedPreferences.
     *
     * @param token The authentication token to be saved.
     */
    fun saveToken(token: String) {
        sharedPreferences.edit { putString(KEY_TOKEN, token) }
    }

    /**
     * Retrieves the authentication token from SharedPreferences.
     *
     * @return The stored authentication token, or null if not found.
     */
    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    /**
     * Saves the refresh token to SharedPreferences.
     *
     * @param refreshToken The refresh token to be saved.
     */
    fun saveRefreshToken(refreshToken: String) {
        sharedPreferences.edit { putString(KEY_REFRESH_TOKEN, refreshToken) }
    }

    /**
     * Retrieves the refresh token from SharedPreferences.
     *
     * @return The stored refresh token, or null if not found.
     */
    fun getRefreshToken(): String? {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }

    /**
     * Saves the account ID to SharedPreferences.
     *
     * @param accountId The account ID to be saved.
     */
    fun saveAccountId(accountId: String) {
        sharedPreferences.edit { putString(KEY_ACCOUNT_ID, accountId) }
    }

    /**
     * Retrieves the account ID from SharedPreferences.
     *
     * @return The stored account ID, or null if not found.
     */
    fun getAccountId(): String? {
        return sharedPreferences.getString(KEY_ACCOUNT_ID, null)
    }

    /**
     * Clears the stored authentication token from SharedPreferences.
     */
    fun clearAccountId() {
        sharedPreferences.edit { remove(KEY_ACCOUNT_ID) }
    }

    /**
     * Clears the stored authentication token, refresh token, and account ID from SharedPreferences.
     * This is typically used during logout to ensure no sensitive information remains stored.
     */
    fun clearTokens() {
        sharedPreferences.edit {
            remove(KEY_TOKEN)
                .remove(KEY_REFRESH_TOKEN)
                .remove(KEY_ACCOUNT_ID)
        }
    }

    /**
     * Checks if an authentication token is currently stored.
     *
     * @return True if a token is stored, false otherwise.
     */
    fun hasToken(): Boolean {
        return getToken() != null
    }
}