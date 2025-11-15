package com.liquotrack.stocksip.features.profilemanagement.profile.data.remote.model

import com.google.gson.annotations.SerializedName
import com.liquotrack.stocksip.features.profilemanagement.profile.domain.model.Profile

data class ProfileResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("fullName")
    val fullName: String,

    @SerializedName("phoneNumber")
    val phoneNumber: String,

    @SerializedName("contactNumber")
    val contactNumber: String,

    @SerializedName("profilePictureUrl")
    val profilePictureUrl: String?,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("assignedRole")
    val assignedRole: String
) {
    fun toProfile() = Profile(
        id = id,
        firstName = firstName,
        lastName = lastName,
        fullName = fullName,
        phoneNumber = phoneNumber,
        contactNumber = contactNumber,
        profilePictureUrl = profilePictureUrl,
        userId = userId,
        assignedRole = assignedRole
    )
}