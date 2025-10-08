package com.liquotrack.stocksip.features.authentication.register.presentation.register

import androidx.lifecycle.ViewModel
import com.liquotrack.stocksip.features.authentication.login.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible

    private val _confirmPasswordVisible = MutableStateFlow(false)
    val confirmPasswordVisible: StateFlow<Boolean> = _confirmPasswordVisible

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _validationError = MutableStateFlow<String?>(null)
    val validationError: StateFlow<String?> = _validationError

    private val _canProceed = MutableStateFlow(false)
    val canProceed: StateFlow<Boolean> = _canProceed

    /**
     * Updates the full name value
     */
    fun updateFullName(value: String) {
        _fullName.value = value
        validateFields()
    }

    /**
     * Updates the email value
     */
    fun updateEmail(value: String) {
        _email.value = value
        validateFields()
    }

    /**
     * Updates the password value
     */
    fun updatePassword(value: String) {
        _password.value = value
        validateFields()
    }

    /**
     * Updates the confirm password value
     */
    fun updateConfirmPassword(value: String) {
        _confirmPassword.value = value
        validateFields()
    }

    /**
     * Toggles password visibility
     */
    fun togglePasswordVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }

    /**
     * Toggles confirm password visibility
     */
    fun toggleConfirmPasswordVisibility() {
        _confirmPasswordVisible.value = !_confirmPasswordVisible.value
    }

    /**
     * Validates all fields before proceeding
     */
    private fun validateFields() {
        _validationError.value = null

        when {
            _fullName.value.isBlank() -> {
                _validationError.value = "Full name is required"
                _canProceed.value = false
                return
            }
            _fullName.value.length < 3 -> {
                _validationError.value = "Full name must be at least 3 characters"
                _canProceed.value = false
                return
            }
            _email.value.isBlank() -> {
                _validationError.value = "Email is required"
                _canProceed.value = false
                return
            }
            !isValidEmail(_email.value) -> {
                _validationError.value = "Invalid email format"
                _canProceed.value = false
                return
            }
            _password.value.isBlank() -> {
                _validationError.value = "Password is required"
                _canProceed.value = false
                return
            }
            _password.value.length < 8 -> {
                _validationError.value = "Password must be at least 8 characters"
                _canProceed.value = false
                return
            }
            _confirmPassword.value.isBlank() -> {
                _validationError.value = "Please confirm your password"
                _canProceed.value = false
                return
            }
            _password.value != _confirmPassword.value -> {
                _validationError.value = "Passwords do not match"
                _canProceed.value = false
                return
            }
            else -> {
                _canProceed.value = true
            }
        }
    }

    /**
     * Validates email format
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Proceeds to next step (account registration)
     * Returns true if validation passed
     */
    fun proceedToAccountRegistration(): Boolean {
        validateFields()
        return _canProceed.value
    }

    /**
     * Clears error message
     */
    fun clearError() {
        _errorMessage.value = null
        _validationError.value = null
    }

    /**
     * Gets the username from full name (removes spaces and converts to lowercase)
     */
    fun getUsername(): String {
        return _fullName.value.lowercase().replace(" ", "")
    }
}