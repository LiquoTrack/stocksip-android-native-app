package com.liquotrack.stocksip.features.profilemanagement.profile.data.remote.services

import com.liquotrack.stocksip.features.profilemanagement.profile.data.repositories.ProfileResponse
import com.liquotrack.stocksip.features.profilemanagement.profile.data.repositories.UpdateProfileRequest
import com.liquotrack.stocksip.features.profilemanagement.profile.data.repositories.UploadImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileService {

    @GET("api/v1/profiles")
    suspend fun getProfileByUserId(@Query("userId") userId: String): ProfileResponse

    @GET("api/v1/profiles/{id}")
    suspend fun getProfileById(@Path("id") profileId: String): ProfileResponse

    @Multipart
    @POST("api/v1/profiles")
    suspend fun createProfile(
        @Part("FirstName") firstName: RequestBody,
        @Part("LastName") lastName: RequestBody,
        @Part("PhoneNumber") phoneNumber: RequestBody,
        @Part("UserId") userId: RequestBody,
        @Part("AssignedRole") assignedRole: RequestBody,
        @Part profilePicture: MultipartBody.Part? = null
    ): ProfileResponse

    @Multipart
    @PUT("api/v1/profiles/{id}")
    suspend fun updateProfile(
        @Path("id") profileId: String,
        @Part("FirstName") firstName: RequestBody? = null,
        @Part("LastName") lastName: RequestBody? = null,
        @Part("PhoneNumber") phoneNumber: RequestBody? = null,
        @Part("AssignedRole") assignedRole: RequestBody? = null,
        @Part profilePicture: MultipartBody.Part? = null
    ): ProfileResponse

    @DELETE("api/v1/profiles/{id}")
    suspend fun deleteProfile(@Path("id") profileId: String)
}