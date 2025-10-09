package com.liquotrack.stocksip.features.authentication.login.data.remote.model

import com.google.gson.annotations.SerializedName

data class SignInResponseDto(
    @SerializedName("user")
    val user: UserDto,

    @SerializedName("token")
    val token: String,
)
