package com.liquotrack.stocksip.features.authentication.adminpanel.data.remote.models


import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing a wrapper for sub-user information.
 *
 * @property maxUsersAllowed The maximum number of users allowed.
 * @property totalUsers The total number of users currently present.
 * @property users A list of User objects representing the sub-users.
 */
data class SubUserWrapperDto(
    @SerializedName("maxUsersAllowed")
    val maxUsersAllowed: Int,
    @SerializedName("totalUsers")
    val totalUsers: Int,
    @SerializedName("users")
    val users: List<UserDto>
)