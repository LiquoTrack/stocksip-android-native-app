package com.liquotrack.stocksip.features.authentication.register.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.common.utils.Resource
import com.liquotrack.stocksip.shared.domain.model.User
import com.liquotrack.stocksip.features.authentication.login.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterAccountViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _selectedRole = MutableStateFlow("Owner")
    val selectedRole: StateFlow<String> = _selectedRole

    private val _businessName = MutableStateFlow("")
    val businessName: StateFlow<String> = _businessName

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _validationError = MutableStateFlow<String?>(null)
    val validationError: StateFlow<String?> = _validationError

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess

    /**
     * Updates the selected role
     */
    fun updateSelectedRole(role: String) {
        _selectedRole.value = role
        validateFields()
    }

    /**
     * Updates the business name
     */
    fun updateBusinessName(value: String) {
        _businessName.value = value
        validateFields()
    }

    /**
     * Validates business name field
     */
    private fun validateFields() {
        _validationError.value = null

        when {
            _businessName.value.isBlank() -> {
                _validationError.value = "Business name is required"
                return
            }
            _businessName.value.length < 3 -> {
                _validationError.value = "Business name must be at least 3 characters"
                return
            }
        }
    }

    /**
     * Registers the complete account with user info and business info
     */
    fun register(email: String, username: String, password: String) {
        viewModelScope.launch {
            validateFields()

            if (_validationError.value != null) {
                return@launch
            }

            _isLoading.value = true
            _errorMessage.value = null

            val resource = repository.register(
                email = email,
                username = username,
                password = password,
                userRole = _selectedRole.value
            )

            when (resource) {
                is Resource.Success -> {
                    _user.value = resource.data
                    _registrationSuccess.value = true
                }
                is Resource.Error -> {
                    _errorMessage.value = resource.message
                }
                is Resource.Loading -> {
                    // Already handled with _isLoading
                }
            }

            _isLoading.value = false
        }
    }

    /**
     * Clears error message
     */
    fun clearError() {
        _errorMessage.value = null
        _validationError.value = null
    }

    /**
     * Resets registration success state
     */
    fun resetRegistrationSuccess() {
        _registrationSuccess.value = false
    }
}