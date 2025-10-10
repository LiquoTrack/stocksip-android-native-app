package com.liquotrack.stocksip.core.navigation

/**
 * Sealed class defining all navigation routes in the app.
 * Includes authentication, admin, and content sections.
 */
sealed class Route(val route: String) {

    // Authentication
    object Login : Route(route = "login")
    object Register : Route(route = "register")

    // Register Account (with arguments)
    object RegisterAccount : Route(route = "register_account") {
        const val routeWithArguments = "register_account/{email}/{fullName}/{password}"
        const val emailArg = "email"
        const val fullNameArg = "fullName"
        const val passwordArg = "password"
    }

    // Password Recovery (email required)
    object PasswordRecovery : Route(route = "password_recovery")

    // Confirmation Code (email required)
    object ConfirmationCode : Route(route = "confirmation_code") {
        const val routeWithArguments = "confirmation_code/{email}"
        const val emailArg = "email"
    }

    // Main app routes
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
 * Drawer routes used for navigation in the Navigation Drawer.
 * These correspond to the route identifiers defined in DrawerNavigationHandler.
 */
object DrawerRoutes {
    const val HOME = "home"
    const val WAREHOUSE = "warehouse"
    const val CARE_GUIDES = "care_guides"
    const val ORDERS = "orders"
    const val PRODUCTS = "products"
    const val CATALOG = "catalog"
    const val PLANS = "plans"
    const val ADMIN = "admin"
    const val PROFILE = "profile"
    const val LOGOUT = "logout"
}