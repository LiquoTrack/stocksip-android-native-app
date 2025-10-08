package com.liquotrack.stocksip.features.authentication.login.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.common.utils.Resource
import com.liquotrack.stocksip.features.authentication.login.domain.model.User
import com.liquotrack.stocksip.features.authentication.login.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun updateEmail(value: String) {
        _email.value = value
    }

    fun updatePassword(value: String) {
        _password.value = value
    }

    fun togglePasswordVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }

    fun login() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val resource = repository.login(_email.value, _password.value)

            when (resource) {
                is Resource.Success -> {
                    _user.value = resource.data
                }
                is Resource.Error -> {
                    _errorMessage.value = resource.message
                }
                is Resource.Loading -> {
                }
            }

            _isLoading.value = false
        }
    }

    fun forgotPassword() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val resource = repository.forgotPassword(_email.value)

            when (resource) {
                is Resource.Success -> {
                    _errorMessage.value = "Email sent. Please check your inbox."
                }
                is Resource.Error -> {
                    _errorMessage.value = resource.message
                }
                is Resource.Loading -> {
                }
            }

            _isLoading.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}