data class Alert(
    val id: String,
    val title: String,
    val message: String,
    val severity: ESeverityTypes,
    val type: EAlertTypes,
    val accountId: String,
    val inventoryId: String,
    val generatedAt: String // Usa DateTime si lo mapeas con Gson/ Moshi
)

enum class EAlertTypes {
    ProductLowStock, ProductOutOfStock, ProductExpired
}

enum class ESeverityTypes {
    Info, Warning, Critical
}
