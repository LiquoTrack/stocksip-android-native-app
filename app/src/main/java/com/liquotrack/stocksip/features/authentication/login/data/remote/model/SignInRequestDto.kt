package com.liquotrack.stocksip.features.authentication.login.data.remote.model

import com.google.gson.annotations.SerializedName

data class SignInRequestDto(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)
