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

    private val _profileId = MutableStateFlow("")
    val profileId: StateFlow<String> = _profileId.asStateFlow()

    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName.asStateFlow()

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _contactNumber = MutableStateFlow("")
    val contactNumber: StateFlow<String> = _contactNumber.asStateFlow()

    private val _assignedRole = MutableStateFlow("")
    val assignedRole: StateFlow<String> = _assignedRole.asStateFlow()

    private val _profilePictureUrl = MutableStateFlow<String?>(null)
    val profilePictureUrl: StateFlow<String?> = _profilePictureUrl.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    // Store original values for cancel functionality
    private var originalFirstName = ""
    private var originalLastName = ""
    private var originalPhoneNumber = ""
    private var originalAssignedRole = ""
    private var originalProfilePictureUrl: String? = null

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getProfile().collect { profile ->
                    _profileId.value = profile.id
                    _firstName.value = profile.firstName
                    _lastName.value = profile.lastName
                    _fullName.value = profile.fullName
                    _phoneNumber.value = profile.phoneNumber
                    _contactNumber.value = profile.contactNumber
                    _assignedRole.value = profile.assignedRole
                    _profilePictureUrl.value = profile.profilePictureUrl

                    // Save original values
                    originalFirstName = profile.firstName
                    originalLastName = profile.lastName
                    originalPhoneNumber = profile.phoneNumber
                    originalAssignedRole = profile.assignedRole
                    originalProfilePictureUrl = profile.profilePictureUrl
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load profile"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateFirstName(newFirstName: String) {
        _firstName.value = newFirstName
    }

    fun updateLastName(newLastName: String) {
        _lastName.value = newLastName
    }

    fun updatePhoneNumber(newPhoneNumber: String) {
        _phoneNumber.value = newPhoneNumber
    }

    fun updateAssignedRole(newRole: String) {
        _assignedRole.value = newRole
    }

    fun updateProfileImage(uri: Uri) {
        _selectedImageUri.value = uri
    }

    fun toggleEditMode() {
        if (_isEditMode.value) {
            // Cancel editing - restore original values
            _firstName.value = originalFirstName
            _lastName.value = originalLastName
            _phoneNumber.value = originalPhoneNumber
            _assignedRole.value = originalAssignedRole
            _profilePictureUrl.value = originalProfilePictureUrl
            _selectedImageUri.value = null
        }
        _isEditMode.value = !_isEditMode.value
    }

    fun saveProfile() {
        viewModelScope.launch {
            _isSaving.value = true
            _errorMessage.value = null
            _successMessage.value = null

            try {
                val updatedProfile = repository.updateProfile(
                    profileId = _profileId.value,
                    firstName = _firstName.value.takeIf { it.isNotBlank() },
                    lastName = _lastName.value.takeIf { it.isNotBlank() },
                    phoneNumber = _phoneNumber.value.takeIf { it.isNotBlank() },
                    assignedRole = _assignedRole.value.takeIf { it.isNotBlank() },
                    profilePictureUri = _selectedImageUri.value
                )

                // Update all values with the response from server
                _profileId.value = updatedProfile.id
                _firstName.value = updatedProfile.firstName
                _lastName.value = updatedProfile.lastName
                _fullName.value = updatedProfile.fullName
                _phoneNumber.value = updatedProfile.phoneNumber
                _contactNumber.value = updatedProfile.contactNumber
                _assignedRole.value = updatedProfile.assignedRole
                _profilePictureUrl.value = updatedProfile.profilePictureUrl

                // Update original values after successful save
                originalFirstName = updatedProfile.firstName
                originalLastName = updatedProfile.lastName
                originalPhoneNumber = updatedProfile.phoneNumber
                originalAssignedRole = updatedProfile.assignedRole
                originalProfilePictureUrl = updatedProfile.profilePictureUrl

                _selectedImageUri.value = null
                _isEditMode.value = false
                _successMessage.value = "Profile updated successfully"
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to save profile"
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearSuccess() {
        _successMessage.value = null
    }
}