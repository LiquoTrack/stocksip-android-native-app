package com.liquotrack.stocksip.features.profilemanagement.profile.domain.model

data class Profile(
    val id: String,
    val name: String,
    val email: String,
    val contactNumber: String,
    val profileImageUrl: String?
)
