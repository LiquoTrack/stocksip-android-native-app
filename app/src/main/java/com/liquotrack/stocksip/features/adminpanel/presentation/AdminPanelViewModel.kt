package com.liquotrack.stocksip.features.adminpanel.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.adminpanel.domain.repositories.UserRepository
import com.liquotrack.stocksip.shared.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminPanelViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedTab = MutableStateFlow(AdminTab.USERS)
    val selectedTab: StateFlow<AdminTab> = _selectedTab.asStateFlow()

    val userToEdit = mutableStateOf<User?>(null)
    val userToDelete = mutableStateOf<User?>(null)

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getAllUsers().collect { usersList ->
                    _users.value = usersList
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load users"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectTab(tab: AdminTab) {
        _selectedTab.value = tab
    }

    fun selectUserForEdit(user: User) {
        userToEdit.value = user
    }

    fun clearUserToEdit() {
        userToEdit.value = null
    }

    fun selectUserForDelete(user: User) {
        userToDelete.value = user
    }

    fun clearUserToDelete() {
        userToDelete.value = null
    }

    fun createUser(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.createUser(user)
                loadUsers()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to create user"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.updateUser(user)
                loadUsers()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to update user"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteUser(user.accountId)
                loadUsers()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to delete user"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}