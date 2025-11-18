package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models

data class SupplierInfo(
    val account: AccountInfo,
    val catalogs: List<Catalog>
)