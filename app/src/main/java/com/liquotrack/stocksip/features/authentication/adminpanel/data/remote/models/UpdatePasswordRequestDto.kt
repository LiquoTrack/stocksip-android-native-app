package com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models


import com.google.gson.annotations.SerializedName

/**
 * Data transfer object for updating a user's password.
 *
 * @param email The email of the user whose password is to be updated.
 * @param newPassword The new password to set for the user.
 */
data class UpdatePasswordRequestDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("newPassword")
    val newPassword: String
)