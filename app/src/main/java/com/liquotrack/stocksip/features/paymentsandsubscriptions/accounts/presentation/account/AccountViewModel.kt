package com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.domain.repositories.AccountRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AccountRepository,
    private val tokenModel: TokenManager
) : ViewModel() {

    private val _accountStatus = MutableStateFlow<String?>(null)
    val accountStatus: StateFlow<String?> = _accountStatus.asStateFlow()

    private val _accountRole = MutableStateFlow<String?>(tokenModel.getAccountRole())
    val accountRole: StateFlow<String?> = _accountRole.asStateFlow()

    fun fetchAccountStatus() {
        viewModelScope.launch {
            try {
                val accountId = tokenModel.getAccountId() ?: throw Exception("Account ID not found")
                val status = repository.getAccountStatus(accountId)
                _accountStatus.value = status
            } catch (e: Exception) {
                _accountStatus.value = "Not Available"
            }
        }
    }

    fun loadAccountRoleFromStorage() {
        _accountRole.value = tokenModel.getAccountRole()
    }

    fun setAccountRole(role: String) {
        tokenModel.saveAccountRole(role)
        _accountRole.value = role
    }

    fun fetchAccountRole() {
        viewModelScope.launch {
            try {
                val accountId = tokenModel.getAccountId() ?: throw Exception("Account ID not found")
                val role = repository.getAccountRole(accountId)
                tokenModel.saveAccountRole(role)
                _accountRole.value = role
            } catch (e: Exception) {
                throw e
            }
        }
    }
}