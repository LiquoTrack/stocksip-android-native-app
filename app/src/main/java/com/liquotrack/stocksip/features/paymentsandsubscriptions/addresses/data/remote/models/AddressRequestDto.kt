package com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.remote.models

import com.google.gson.annotations.SerializedName

data class AddressRequestDto(
    @SerializedName("street")
    val street: String,

    @SerializedName("city")
    val city: String,

    @SerializedName("state")
    val state: String? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("zipCode")
    val zipCode: String? = null
)