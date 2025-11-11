package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.data.remote.models

import com.google.gson.annotations.SerializedName

data class SupplierInfoDto(
    @SerializedName("account")
    val account: AccountInfoDto,

    @SerializedName("catalogs")
    val catalogs: List<CatalogDto>
)