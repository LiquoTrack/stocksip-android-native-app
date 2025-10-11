package com.liquotrack.stocksip.features.profilemanagement.profile.data.remote.model

data class ProfileDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val phoneNumber: String,
    val contactNumber: String,
    val profilePictureUrl: String?,
    val userId: String,
    val assignedRole: String
)
