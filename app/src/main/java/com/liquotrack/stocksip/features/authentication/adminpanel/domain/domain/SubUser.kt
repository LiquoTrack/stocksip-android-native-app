package com.liquotrack.stocksip.features.authentication.adminpanel.domain.domain

/**
 * Data class representing a sub-user in the admin panel.
 *
 * @property id The unique identifier of the sub-user.
 * @property email The email address of the sub-user.
 * @property userRole The role of the user in the system.
 * @property profileId The profile ID associated with the sub-user.
 * @property fullName The full name of the sub-user.
 * @property phoneNumber The phone number of the sub-user.
 * @property profilePictureUrl The URL of the sub-user's profile picture.
 * @property profileRole The role associated with the sub-user's profile.
 */
data class SubUser(
    val id: String,
    val email: String,
    val userRole: String,
    val profileId: String,
    val fullName: String,
    val phoneNumber: String,
    val profilePictureUrl: String,
    val profileRole: String,
    val password: String = ""
)
