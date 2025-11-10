package com.liquotrack.stocksip.features.authentication.passwordrecover.data.remote.models


import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for recovery code request.
 *
 * @property email The email address to send the recovery code to.
 */
data class RecoveryCodeRequestDto(
    @SerializedName("email")
    val email: String
)