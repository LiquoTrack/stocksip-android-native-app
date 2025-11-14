package com.liquotrack.stocksip.features.alerts.data.remote.models

data class Alert(
    val id: String,
    val title: String,
    val message: String,
    val severity: ESeverityTypes,
    val type: EAlertTypes,
    val accountId: String,
    val inventoryId: String
)

enum class EAlertTypes {
    ProductLowStock, ProductOutOfStock, ProductExpired
}

enum class ESeverityTypes {
    Info, Warning, Critical
}