package com.liquotrack.stocksip.features.authentication.passwordrecover.data.remote.models


import com.google.gson.annotations.SerializedName

/**
 * Data transfer object representing the response for a confirmation code request.
 *
 * @property message The message returned from the server.
 */
data class ConfirmationCodeResponseDto(
    @SerializedName("message")
    val message: String
)