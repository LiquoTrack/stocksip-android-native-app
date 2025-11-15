package com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models


import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing a User.
 *
 * @property email The email address of the user.
 * @property fullName The full name of the user.
 * @property phoneNumber The phone number of the user.
 * @property profileId The profile ID associated with the user.
 * @property profilePictureUrl The URL of the user's profile picture.
 * @property profileRole The role associated with the user's profile.
 * @property role The general role of the user.
 * @property userId The unique identifier for the user.
 */
data class UserDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("profileId")
    val profileId: String,
    @SerializedName("profilePictureUrl")
    val profilePictureUrl: String,
    @SerializedName("profileRole")
    val profileRole: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("userId")
    val userId: String
)