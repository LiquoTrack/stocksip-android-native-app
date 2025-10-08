package com.liquotrack.stocksip.core.navigation

/**
 * Sealed class representing different routes in the application.
 */
sealed class Route(val route: String) {
    // Route for the login screen
    object Login : Route(route = "login")

    // Route for the main screen
    object Main : Route(route = "main")

    // Route for the warehouses screen
    object Warehouses : Route(route = "warehouses") {
        const val routeWithArgument = "warehouses/{id}"
        const val argument = "id"
    }

    // Route for the products/storage screen
    object Products : Route(route = "products_storage") {
        const val routeWithArgument = "products_storage/{id}"
        const val argument = "id"
    }

    // Route for the product detail screen
    object ProductDetail : Route(route = "product_detail") {
        const val routeWithArgument = "product_detail/{id}"
        const val argument = "id"
    }

    // Route for the alerts screen
    object Alerts : Route(route = "alerts") {
        const val routeWithArgument = "alerts/{id}"
        const val argument = "id"
    }

    // Route for the inventory (warehouse's products) screen
    object Inventory : Route(route = "inventory") {
        const val routeWithArgument = "inventory/{id}"
        const val argument = "id"
    }

    // Route for the catalog screen
    object Catalogs : Route(route = "catalogs") {
        const val routeWithArgument = "catalogs/{id}"
        const val argument = "id"
    }

    // Route for the catalog detail screen
    object CatalogDetail : Route(route = "catalog_detail") {
        const val routeWithArgument = "catalog_detail/{id}"
        const val argument = "id"
    }

    // Route for the care guides screen
    object CareGuides : Route(route = "care_guide") {
        const val routeWithArgument = "care_guide/{id}"
        const val argument = "id"
    }

    // Route for the user management screen (Admin Panel)
    object UserManagement : Route(route = "user") {
        const val routeWithArgument = "user/{id}"
        const val argument = "id"
    }

    // Route for the profile screen
    object Profile : Route(route = "profile") {
        const val routeWithArgument = "profile/{id}"
        const val argument = "id"
    }

    // Route for the plans screen ⭐ NUEVO
    object Plans : Route(route = "plans") {
        const val routeWithArgument = "plans/{id}"
        const val argument = "id"
    }

    // Route for the making orders screen
    object MakingOrders : Route(route = "making_orders") {
        const val routeWithArgument = "making_orders/{id}"
        const val argument = "id"
    }

    // Route for the order detail screen
    object OrderDetail : Route(route = "order_detail") {
        const val routeWithArgument = "order_detail/{id}"
        const val argument = "id"
    }

    // Route for the order history screen
    object OrderHistory : Route(route = "order_history") {
        const val routeWithArgument = "order_history/{id}"
        const val argument = "id"
    }

    // Route for the product transfer screen
    object ProductTransferHistory : Route(route = "transfers") {
        const val routeWithArgument = "transfers/{id}"
        const val argument = "id"
    }

    // Route for the product exit screen
    object ProductExitHistory : Route(route = "exits") {
        const val routeWithArgument = "exits/{id}"
        const val argument = "id"
    }
}

/**
 * Helper object to simplify route navigation with arguments
 */
object NavigationHelper {

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
    const val ADMIN = "admin"
    const val PROFILE = "profile"
}