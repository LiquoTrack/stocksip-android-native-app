package com.liquotrack.stocksip.features.profilemanagement.profile.domain.repositories

import android.net.Uri
import com.liquotrack.stocksip.features.profilemanagement.profile.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Profile>

    suspend fun updateProfile(
        name: String,
        email: String,
        contactNumber: String,
        profileImageUrl: String?
    )

    suspend fun uploadProfileImage(uri: Uri): String
}