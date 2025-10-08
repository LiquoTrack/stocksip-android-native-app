package com.liquotrack.stocksip.core.navigation

/**
 * Sealed class representing different routes in the application.
 */
sealed class Route(val route: String) {
    // Route for the login screen
    object Login : Route(route = "login")

    // Route for the sign-up user screen
    object SignUpUser : Route(route = "signup-user")

    // Route for the sign-up account screen
    object  SignUpAccount : Route(route = "signup-account")

    // Route for the main screen
    object Main : Route(route = "main")

    // Route for the warehouses screen
    object Warehouses : Route(route = "warehouses") {
        const val routeWithArgument = "account/{id}"
        const val argument = "id"
    }

    // Route for the products/storage screen
    object Products : Route(route = "products_storage") {
        const val routeWithArgument = "account/{id}"
        const val argument = "id"
    }

    // Route for the product detail screen
    object ProductDetail : Route(route = "product_detail") {
        const val routeWithArgument = "product_detail/{id}"
        const val argument = "id"
    }

    // Route for the alerts screen
    object Alerts : Route(route = "alerts") {
        const val routeWithArgument = "account/{id}"
        const val argument = "id"
    }

    // Route for the inventory (warehouse's products) screen
    object Inventory : Route(route = "inventory") {
        const val routeWithArgument = "inventory/{id}"
        const val argument = "id"
    }

    // Route for the catalog screen
    object Catalogs : Route(route = "catalogs") {
        const val routeWithArgument = "account/{id}"
        const val argument = "id"
    }

    // Route for the catalog detail screen
    object CatalogDetail : Route(route = "catalog_detail") {
        const val routeWithArgument = "catalog_detail/{id}"
        const val argument = "id"
    }

    // Route for the care guides screen
    object CareGuides : Route(route = "care_guide") {
        const val routeWithArgument = "account/{id}"
        const val argument = "id"
    }

    // Route for the user management screen
    object UserManagement : Route(route = "user") {
        const val routeWithArgument = "user/{id}"
        const val argument = "id"
    }

    // Route for the profile screen
    object Profile : Route(route = "profile") {
        const val routeWithArgument = "user/{id}"
        const val argument = "id"
    }

    // Route for the making orders screen
    object MakingOrders : Route(route = "making_orders") {
        const val routeWithArgument = "account/{id}"
        const val argument = "id"
    }

    // Route for the order detail screen
    object OrderDetail : Route(route = "order_detail") {
        const val routeWithArgument = "order_detail/{id}"
        const val argument = "id"
    }

    // Route for the order history screen
    object OrderHistory : Route(route = "order_history") {
        const val routeWithArgument = "account/{id}"
        const val argument = "id"
    }

    // Route for the product transfer screen
    object ProductTransferHistory : Route(route = "transfers") {
        const val routeWithArgument = "account/{id}"
        const val argument = "id"
    }

    // Route for the product exit screen
    object ProductExitHistory : Route(route = "exits") {
        const val routeWithArgument = "account/{id}"
        const val argument = "id"
    }

}