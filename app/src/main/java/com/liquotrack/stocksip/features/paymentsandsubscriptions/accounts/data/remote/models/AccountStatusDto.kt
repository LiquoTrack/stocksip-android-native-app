package com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.data.remote.models


import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing the account status.
 * @param accountStatus The status of the account as a string.
 */
data class AccountStatusDto(
    @SerializedName("accountStatus")
    val accountStatus: String
)