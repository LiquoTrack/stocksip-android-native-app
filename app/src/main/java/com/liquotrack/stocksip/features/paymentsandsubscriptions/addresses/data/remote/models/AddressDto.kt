package com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.remote.models

import com.google.gson.annotations.SerializedName

data class AddressDto(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("street")
    val street: String? = null,

    @SerializedName("city")
    val city: String? = null,

    @SerializedName("state")
    val state: String? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("zipCode")
    val zipCode: String? = null
)