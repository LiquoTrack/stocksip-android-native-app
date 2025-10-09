package com.liquotrack.stocksip.core.navigation

/**
 * Sealed class representing different routes in the application.
 * Simplified version for testing - routes don't require arguments.
 */
sealed class Route(val route: String) {
    // Authentication routes
    object Login : Route(route = "login")
    object Register : Route(route = "register")

    // Register account with arguments
    object RegisterAccount : Route(route = "register_account") {
        const val routeWithArguments = "register_account/{email}/{fullName}/{password}"
        const val emailArg = "email"
        const val fullNameArg = "fullName"
        const val passwordArg = "password"
    }

    // Main app routes - SIMPLIFIED (no arguments required for testing)
    object Main : Route(route = "main")
    object Warehouses : Route(route = "warehouses")
    object Products : Route(route = "products_storage")
    object ProductDetail : Route(route = "product_detail")
    object Alerts : Route(route = "alerts")
    object Inventory : Route(route = "inventory")
    object Catalogs : Route(route = "catalogs")
    object CatalogDetail : Route(route = "catalog_detail")
    object CareGuides : Route(route = "care_guide")
    object UserManagement : Route(route = "user")
    object Profile : Route(route = "profile")
    object Plans : Route(route = "plans")
    object MakingOrders : Route(route = "making_orders")
    object OrderDetail : Route(route = "order_detail")
    object OrderHistory : Route(route = "order_history")
    object ProductTransferHistory : Route(route = "transfers")
    object ProductExitHistory : Route(route = "exits")
}

/**
 * Helper object to simplify route navigation with arguments
 * For future use when implementing full authentication
 */
object NavigationHelper {

    // Register account with parameters
    fun getRegisterAccountRoute(email: String, fullName: String, password: String) =
        "register_account/$email/$fullName/$password"

    // Future: Routes with IDs (for when implementing full features)
    fun getWarehouseRoute(id: String) = "warehouses/$id"
    fun getProductsRoute(id: String) = "products_storage/$id"
    fun getProductDetailRoute(id: String) = "product_detail/$id"
    fun getAlertsRoute(id: String) = "alerts/$id"
    fun getInventoryRoute(id: String) = "inventory/$id"
    fun getCatalogsRoute(id: String) = "catalogs/$id"
    fun getCatalogDetailRoute(id: String) = "catalog_detail/$id"
    fun getCareGuidesRoute(id: String) = "care_guide/$id"
    fun getUserManagementRoute(id: String) = "user/$id"
    fun getProfileRoute(id: String) = "profile/$id"
    fun getPlansRoute(id: String) = "plans/$id"
    fun getMakingOrdersRoute(id: String) = "making_orders/$id"
    fun getOrderDetailRoute(id: String) = "order_detail/$id"
    fun getOrderHistoryRoute(id: String) = "order_history/$id"
    fun getProductTransferHistoryRoute(id: String) = "transfers/$id"
    fun getProductExitHistoryRoute(id: String) = "exits/$id"
}

/**
 * Object containing simplified route names for NavigationDrawer
 */
object DrawerRoutes {
    const val HOME = "home"
    const val WAREHOUSE = "warehouse"
    const val CARE_GUIDES = "care_guides"
    const val ORDERS = "orders"
    const val PRODUCTS = "products"
    const val CATALOG = "catalog"
    const val PLANS = "plans"
    const val ADMIN = "user"
    const val PROFILE = "profile"
}