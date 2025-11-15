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
import okhttp3.RequestBody.Companion.toRequestBody
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
        profileId: String,
        firstName: String?,
        lastName: String?,
        phoneNumber: String?,
        assignedRole: String?,
        profilePictureUri: Uri?
    ): Profile {
        var tempFile: File? = null

        try {
            val firstNameBody = firstName?.takeIf { it.isNotBlank() }
                ?.toRequestBody("text/plain".toMediaTypeOrNull())
            val lastNameBody = lastName?.takeIf { it.isNotBlank() }
                ?.toRequestBody("text/plain".toMediaTypeOrNull())
            val phoneNumberBody = phoneNumber?.takeIf { it.isNotBlank() }
                ?.toRequestBody("text/plain".toMediaTypeOrNull())
            val assignedRoleBody = assignedRole?.takeIf { it.isNotBlank() }
                ?.toRequestBody("text/plain".toMediaTypeOrNull())

            val profilePicturePart = profilePictureUri?.let { uri ->
                tempFile = uriToFile(uri)
                val requestBody = tempFile!!.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData(
                    "ProfilePicture",
                    tempFile!!.name,
                    requestBody
                )
            }

            val response = apiService.updateProfile(
                profileId = profileId,
                firstName = firstNameBody,
                lastName = lastNameBody,
                phoneNumber = phoneNumberBody,
                assignedRole = assignedRoleBody,
                profilePicture = profilePicturePart
            )

            return response.toProfile()
        } finally {
            tempFile?.let {
                if (it.exists()) {
                    it.delete()
                }
            }
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open URI: $uri")

        val extension = context.contentResolver.getType(uri)?.let { mimeType ->
            when {
                mimeType.contains("png") -> ".png"
                mimeType.contains("jpg") || mimeType.contains("jpeg") -> ".jpg"
                else -> ".jpg"
            }
        } ?: ".jpg"

        val tempFile = File.createTempFile(
            "profile_upload_${System.currentTimeMillis()}",
            extension,
            context.cacheDir
        )

        FileOutputStream(tempFile).use { output ->
            inputStream.use { input ->
                input.copyTo(output)
            }
        }

        return tempFile
    }
}