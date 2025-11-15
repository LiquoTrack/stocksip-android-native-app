package com.liquotrack.stocksip.features.authentication.passwordrecover.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.authentication.passwordrecover.domain.repositories.RecoverPasswordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for handling password recovery operations.
 */
@HiltViewModel
class RecoverPasswordViewModel @Inject constructor(
    private val recoverPasswordRepository: RecoverPasswordRepository,
) : ViewModel() {

    /**
     * Sends a recovery code to the specified email.
     *
     * @param email The email address to send the recovery code to.
     */
    fun sendRecoveryCode(
        email: String,
        onResult: (Result<String>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val message = recoverPasswordRepository.sendRecoveryCode(email)
                onResult(Result.success(message))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }

    /**
     * Verifies the recovery code for the specified email.
     *
     * @param email The email address associated with the recovery code.
     * @param code The recovery code to verify.
     */
    fun verifyRecoveryCode(
        code: String,
        email: String,
        onResult: (Result<String>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val message = recoverPasswordRepository.verifyRecoveryCode(code, email)
                onResult(Result.success(message))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }

    /**
     * Updates the password for the specified email.
     *
     * @param email The email address to update the password for.
     * @param newPassword The new password to set.
     */
    fun updatePassword(
        email: String,
        newPassword: String,
        onResult: (Result<String>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val message = recoverPasswordRepository.resetPassword(email, newPassword)
                onResult(Result.success(message))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }
}