package com.liquotrack.stocksip.features.profilemanagement.profile.data.remote.services

import com.liquotrack.stocksip.features.profilemanagement.profile.data.remote.model.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileService {
    @GET("profiles/me")
    suspend fun getProfile(): ProfileResponse

    @Multipart
    @PUT("profiles/{profileId}")
    suspend fun updateProfile(
        @Path("profileId") profileId: String,
        @Part("FirstName") firstName: RequestBody?,
        @Part("LastName") lastName: RequestBody?,
        @Part("PhoneNumber") phoneNumber: RequestBody?,
        @Part("AssignedRole") assignedRole: RequestBody?,
        @Part profilePicture: MultipartBody.Part?
    ): ProfileResponse
}