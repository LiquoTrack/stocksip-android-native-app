package com.liquotrack.stocksip.features.authentication.adminpanel.presentation

import androidx.compose.runtime.mutableStateOf
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.R
import com.liquotrack.stocksip.features.authentication.adminpanel.domain.domain.AccountUsers
import com.liquotrack.stocksip.features.authentication.adminpanel.domain.domain.SubUser
import com.liquotrack.stocksip.features.authentication.adminpanel.domain.repositories.UserRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminPanelViewModel @Inject constructor(
    private val repository: UserRepository,
    private val tokenManager: TokenManager,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _users = MutableStateFlow<List<AccountUsers>>(emptyList())
    val users: StateFlow<List<AccountUsers>> = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedTab = MutableStateFlow(AdminTab.ALL)
    val selectedTab: StateFlow<AdminTab> = _selectedTab.asStateFlow()

    private val _userToDelete = MutableStateFlow<SubUser?>(null)
    val userToDelete: StateFlow<SubUser?> = _userToDelete

    private val _userToEdit = MutableStateFlow<SubUser?>(null)
    val userToEdit: StateFlow<SubUser?> = _userToEdit

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadUsers()
    }

    private fun loadUsers(role: String = "All") {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val account = tokenManager.getAccountId() ?: return@launch
                val response = repository.getAllSubUsers(account, role)

                if (response.isSuccessful) {
                    response.body()?.let { accountUsers ->
                        _users.value = listOf(accountUsers)
                    }
                } else {
                    _errorMessage.value = appContext.getString(R.string.error_failed_load_users, response.code())
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: appContext.getString(R.string.error_unexpected)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectTab(tab: AdminTab) {
        _selectedTab.value = tab

        when (tab) {
            AdminTab.ALL -> loadUsers("All")
            AdminTab.ADMIN -> loadUsers("SuperAdmin")
            AdminTab.EMPLOYEE -> loadUsers("Employee")
        }
    }

    fun selectUserForDelete(user: SubUser) {
        _userToDelete.value = user
    }

    fun selectUserForEdit(user: SubUser) {
        _userToEdit.value = user
    }

    fun clearUserToDelete() {
        _userToDelete.value = null
    }

    fun clearUserToEdit() {
        _userToEdit.value = null
    }

    fun createUser(user: SubUser) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.createSubUser(user)
                if (response.isSuccessful) {
                    loadUsers(
                        when (selectedTab.value) {
                            AdminTab.ALL -> "All"
                            AdminTab.ADMIN -> "SuperAdmin"
                            AdminTab.EMPLOYEE -> "Employee"
                        }
                    )
                } else {
                    _errorMessage.value = appContext.getString(R.string.error_failed_create_user, response.code())
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: appContext.getString(R.string.error_unexpected)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateUser(user: SubUser) {
        viewModelScope.launch {
            _isLoading.value = true

        }
    }

    fun deleteUser(user: SubUser) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteUser(userId = user.id, profileId = user.profileId)
                loadUsers(
                    when (selectedTab.value) {
                        AdminTab.ALL -> "All"
                        AdminTab.ADMIN -> "SuperAdmin"
                        AdminTab.EMPLOYEE -> "Employee"
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: appContext.getString(R.string.error_failed_delete_user)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}