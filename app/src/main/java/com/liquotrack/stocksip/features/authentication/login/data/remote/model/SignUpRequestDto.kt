package com.liquotrack.stocksip.features.authentication.login.data.remote.model

import com.google.gson.annotations.SerializedName

data class SignUpRequestDto(
    @SerializedName("email")
    val email: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("userRole")
    val userRole: String
)