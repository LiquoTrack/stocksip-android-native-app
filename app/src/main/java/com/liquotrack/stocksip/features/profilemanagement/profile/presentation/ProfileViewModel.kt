package com.liquotrack.stocksip.features.profilemanagement.profile.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.profilemanagement.profile.domain.repositories.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _contactNumber = MutableStateFlow("")
    val contactNumber: StateFlow<String> = _contactNumber.asStateFlow()

    private val _profileImageUrl = MutableStateFlow<String?>(null)
    val profileImageUrl: StateFlow<String?> = _profileImageUrl.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Store original values for cancel functionality
    private var originalName = ""
    private var originalEmail = ""
    private var originalContactNumber = ""
    private var originalProfileImageUrl: String? = null

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getProfile().collect { profile ->
                    _name.value = profile.name
                    _email.value = profile.email
                    _contactNumber.value = profile.contactNumber
                    _profileImageUrl.value = profile.profileImageUrl

                    // Store original values
                    originalName = profile.name
                    originalEmail = profile.email
                    originalContactNumber = profile.contactNumber
                    originalProfileImageUrl = profile.profileImageUrl
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load profile"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateName(newName: String) {
        _name.value = newName
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updateContactNumber(newNumber: String) {
        _contactNumber.value = newNumber
    }

    fun toggleEditMode() {
        if (_isEditMode.value) {
            // Cancel editing - restore original values
            _name.value = originalName
            _email.value = originalEmail
            _contactNumber.value = originalContactNumber
            _profileImageUrl.value = originalProfileImageUrl
        }
        _isEditMode.value = !_isEditMode.value
    }

    fun saveProfile() {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                repository.updateProfile(
                    name = _name.value,
                    email = _email.value,
                    contactNumber = _contactNumber.value,
                    profileImageUrl = _profileImageUrl.value
                )

                // Update original values after successful save
                originalName = _name.value
                originalEmail = _email.value
                originalContactNumber = _contactNumber.value
                originalProfileImageUrl = _profileImageUrl.value

                _isEditMode.value = false
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to save profile"
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun uploadProfileImage(uri: Uri) {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                val imageUrl = repository.uploadProfileImage(uri)
                _profileImageUrl.value = imageUrl
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to upload image"
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}