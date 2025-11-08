package com.liquotrack.stocksip.features.profilemanagement.profile.domain.repositories

import android.net.Uri
import com.liquotrack.stocksip.features.profilemanagement.profile.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Profile>

    suspend fun updateProfile(
        profileId: String,
        firstName: String?,
        lastName: String?,
        phoneNumber: String?,
        assignedRole: String?,
        profilePictureUri: Uri?
    ): Profile
}