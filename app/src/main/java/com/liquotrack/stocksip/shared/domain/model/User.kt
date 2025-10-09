package com.liquotrack.stocksip.shared.domain.model

import com.google.gson.annotations.SerializedName

data class User(
    val email: String,
    val username: String,
    val userRole: String,
    val accountId: String
)

enum class UserRole {
    @SerializedName("Admin")
    ADMIN,

    @SerializedName("Employee")
    EMPLOYEE,

    @SerializedName("Manager")
    MANAGER,

    @SerializedName("Owner")
    OWNER
}