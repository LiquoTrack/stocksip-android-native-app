package com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.remote.models.AddressDto
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.remote.models.AddressRequestDto
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.repositories.AddressRepositoryImpl
import com.liquotrack.stocksip.shared.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val addressRepository: AddressRepositoryImpl,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _addresses = MutableStateFlow<List<AddressDto>>(emptyList())
    val addresses: StateFlow<List<AddressDto>> = _addresses.asStateFlow()

    private val _selectedAddressIndex = MutableStateFlow<Int?>(null)
    val selectedAddressIndex: StateFlow<Int?> = _selectedAddressIndex.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadAddresses()
    }

    fun loadAddresses() {
        viewModelScope.launch {
            try {
                val accountId = tokenManager.getAccountId()
                    ?: throw Exception("Account ID not found")

                _isLoading.value = true
                _errorMessage.value = null

                addressRepository.getAddresses(accountId)
                    .onSuccess { addressList ->
                        _addresses.value = addressList
                        Log.d("ADDRESS_VM", "Loaded ${addressList.size} addresses")
                    }
                    .onFailure { error ->
                        _errorMessage.value = error.message ?: "Failed to load addresses"
                        Log.e("ADDRESS_VM", "Error loading addresses", error)
                    }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Account ID not available"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectAddress(addressIndex: Int) {
        _selectedAddressIndex.value = addressIndex
        Log.d("ADDRESS_VM", "Selected address index: $addressIndex")
    }

    fun showAddDialog() {
        _showDialog.value = true
    }

    fun hideAddDialog() {
        _showDialog.value = false
    }

    fun addAddress(address: AddressRequestDto) {
        viewModelScope.launch {
            try {
                val accountId = tokenManager.getAccountId()
                    ?: throw Exception("Account ID not found")

                _isLoading.value = true
                _errorMessage.value = null

                addressRepository.addAddress(accountId, address)
                    .onSuccess {
                        Log.d("ADDRESS_VM", "Address added successfully")
                        loadAddresses()
                        hideAddDialog()
                    }
                    .onFailure { error ->
                        _errorMessage.value = error.message ?: "Failed to add address"
                        Log.e("ADDRESS_VM", "Error adding address", error)
                    }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Account ID not available"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}