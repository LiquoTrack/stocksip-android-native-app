package com.liquotrack.stocksip.features.profilemanagement.profile.data.repositories

import android.content.Context
import android.net.Uri
import com.liquotrack.stocksip.features.profilemanagement.profile.data.remote.services.ProfileService
import com.liquotrack.stocksip.features.profilemanagement.profile.domain.model.Profile
import com.liquotrack.stocksip.features.profilemanagement.profile.domain.repositories.ProfileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ProfileService,
    @ApplicationContext private val context: Context
) : ProfileRepository {

    override fun getProfile(): Flow<Profile> = flow {
        val response = apiService.getProfile()
        emit(response.toProfile())
    }

    override suspend fun updateProfile(
        name: String,
        email: String,
        contactNumber: String,
        profileImageUrl: String?
    ) {
        val request = UpdateProfileRequest(
            name = name,
            email = email,
            contactNumber = contactNumber,
            profileImageUrl = profileImageUrl
        )
        apiService.updateProfile(request)
    }

    override suspend fun uploadProfileImage(uri: Uri): String {
        // Convert Uri to File
        val file = uriToFile(uri)

        // Create multipart body
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestBody
        )

        // Upload to your backend (which forwards to Cloudinary)
        val response = apiService.uploadProfileImage(multipartBody)

        // Clean up temp file
        file.delete()

        return response.imageUrl
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open URI")

        val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
        val outputStream = FileOutputStream(tempFile)

        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return tempFile
    }
}

// API Service Interface


// Data Transfer Objects
data class ProfileResponse(
    val id: String,
    val name: String,
    val email: String,
    val contactNumber: String,
    val profileImageUrl: String?
) {
    fun toProfile() = Profile(
        id = id,
        name = name,
        email = email,
        contactNumber = contactNumber,
        profileImageUrl = profileImageUrl
    )
}

data class UpdateProfileRequest(
    val name: String,
    val email: String,
    val contactNumber: String,
    val profileImageUrl: String?
)

data class UploadImageResponse(
    val imageUrl: String,
    val publicId: String
)