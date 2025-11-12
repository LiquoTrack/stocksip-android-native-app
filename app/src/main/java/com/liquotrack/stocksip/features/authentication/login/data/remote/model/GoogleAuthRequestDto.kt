package com.liquotrack.stocksip.features.authentication.login.data.remote.model

import com.google.gson.annotations.SerializedName

data class GoogleAuthRequestDto(
    @SerializedName("idToken")
    val idToken: String,
    @SerializedName("accessToken")
    val accessToken: String? = null,
    @SerializedName("clientId")
    val clientId: String
)
