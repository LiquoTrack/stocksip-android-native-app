package com.liquotrack.stocksip.features.profilemanagement.profile.data.remote.services

import com.liquotrack.stocksip.features.profilemanagement.profile.data.repositories.ProfileResponse
import com.liquotrack.stocksip.features.profilemanagement.profile.data.repositories.UpdateProfileRequest
import com.liquotrack.stocksip.features.profilemanagement.profile.data.repositories.UploadImageResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ProfileService {
    @GET("api/profile")
    suspend fun getProfile(): ProfileResponse

    @PUT("api/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest)

    @Multipart
    @POST("api/profile/upload-image")
    suspend fun uploadProfileImage(
        @Part file: MultipartBody.Part
    ): UploadImageResponse
}