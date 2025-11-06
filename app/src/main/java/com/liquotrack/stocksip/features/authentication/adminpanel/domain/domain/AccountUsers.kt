package com.liquotrack.stocksip.features.authentication.adminpanel.domain.domain

/**
 * Data class representing a collection of account users.
 *
 * @property maxUsersAllowed The maximum number of users allowed for the account.
 * @property totalUsers The total number of users currently associated with the account.
 * @property users A list of SubUser objects representing the users of the account.
 */
data class AccountUsers(
    val maxUsersAllowed: Int,
    val totalUsers: Int,
    val users: List<SubUser>
)
