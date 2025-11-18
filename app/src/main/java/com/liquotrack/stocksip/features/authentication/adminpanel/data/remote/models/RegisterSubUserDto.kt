package com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models


import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for registering a sub-user.
 *
 * @property email The email of the sub-user.
 * @property name The name of the sub-user.
 * @property password The password for the sub-user account.
 * @property phoneNumber The phone number of the sub-user.
 * @property profileRole The profile role assigned to the sub-user.
 * @property role The role of the sub-user within the system.
 */
data class RegisterSubUserDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("profileRole")
    val profileRole: String,
    @SerializedName("role")
    val role: String
)