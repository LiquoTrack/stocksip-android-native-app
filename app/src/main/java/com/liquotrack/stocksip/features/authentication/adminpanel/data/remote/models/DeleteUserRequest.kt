package com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Payload required to delete a user that also owns an associated profile.
 */
data class DeleteUserRequest(
    @SerializedName("profileId")
    val profileId: String
)
