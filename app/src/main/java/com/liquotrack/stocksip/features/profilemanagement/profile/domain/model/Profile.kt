package com.liquotrack.stocksip.features.profilemanagement.profile.domain.model

data class Profile(
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