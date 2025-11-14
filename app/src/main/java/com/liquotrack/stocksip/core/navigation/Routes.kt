package com.liquotrack.stocksip.core.navigation

import android.net.Uri
import java.net.URLEncoder

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
        fun buildRoute(email: String, fullName: String, password: String): String {
            val encodedEmail = Uri.encode(email)
            val encodedFullName = Uri.encode(fullName)
            val encodedPassword = Uri.encode(password)
            return "register_account/$encodedEmail/$encodedFullName/$encodedPassword"
        }
    }

    // Password Recovery (email required)
    object PasswordRecovery : Route(route = "password_recovery")

    // Confirmation Code (email required)
    object ConfirmationCode : Route(route = "confirmation_code") {
        const val routeWithArguments = "confirmation_code/{email}"
        const val emailArg = "email"
    }

    object UpdatePassword : Route(route = "update_password") {
        const val routeWithArguments = "update_password/{email}"
        const val emailArg = "email"
    }

    // Main app routes
    object Main : Route(route = "main")
    object Warehouses : Route(route = "warehouses")

    object WarehouseCreateEdit {
        const val route = "warehouse_create_edit"
        const val warehouseIdArg = "warehouseId"
        val routeWithArgs = "$route/{$warehouseIdArg}"
    }

    object Products : Route(route = "products_storage")

    object ProductCreateEdit {
        const val route = "product_create_edit"
        const val productIdArg = "productId"
        val routeWithArgs = "$route/{$productIdArg}"
    }

    object ProductDetail {
        const val route = "product_detail"
        const val productIdArg = "productId"
        val routeWithArgs = "$route/{$productIdArg}"
    }

    object Alerts : Route(route = "alerts")
    object Addresses : Route(route = "addresses")

    object Inventory {
        const val route = "warehouse_inventory"
        const val warehouseIdArg = "warehouseId"
        val routeWithArgs = "$route/{$warehouseIdArg}"
    }

    object InventoryAddition {
        const val route = "inventory_addition"
        const val warehouseIdArg = "warehouseId"
        val routeWithArgs = "$route/{$warehouseIdArg}"
    }

    object InventorySubtrack {
        const val route = "inventory_subtrack"
        const val warehouseIdArg = "warehouseId"
        val routeWithArgs = "$route/{$warehouseIdArg}"
    }

    object InventoryTransfer {
        const val route = "inventory_transfer"
        const val warehouseIdArg = "warehouseId"
        val routeWithArgs = "$route/{$warehouseIdArg}"
    }

    object InventoryDetails {
        const val route = "inventory_detail"
        const val inventoryIdArg = "inventoryId"
        val routeWithArgs = "$route/{$inventoryIdArg}"
    }

    object CareGuides : Route(route = "care_guide")
    object CareGuideCreate : Route(route = "care_guide_create")
    object CareGuideEdit : Route(route = "care_guide_edit") {
        const val routeWithArguments = "care_guide_edit/{careGuideId}"
        const val careGuideIdArg = "careGuideId"
        fun buildRoute(careGuideId: String): String = "care_guide_edit/$careGuideId"
    }

    object Catalogs : Route("catalogs")

    object CatalogDetail : Route("catalog_detail") {
        const val routeWithArguments = "catalog_detail/{catalogId}"
        const val catalogIdArg = "catalogId"
        fun buildRoute(catalogId: String): String = "catalog_detail/$catalogId"
    }

    object CatalogCreateEdit : Route("catalog_create_edit") {
        const val routeWithArguments = "catalog_create_edit/{catalogId}"
        const val catalogIdArg = "catalogId"
        fun buildRoute(catalogId: String): String = "catalog_create_edit/$catalogId"
    }

    // Store Owner
    object SupplierSearch : Route("supplier_search")

    object SupplierCatalogList : Route("supplier_catalog_list") {
        const val routeWithArguments = "supplier_catalog_list/{supplierId}"
        const val supplierIdArg = "supplierId"
        fun buildRoute(supplierId: String): String =
            "supplier_catalog_list/${Uri.encode(supplierId)}"
    }

    object CatalogDetailView : Route("catalog_detail_view") {
        const val routeWithArguments = "catalog_detail_view/{catalogId}"
        const val catalogIdArg = "catalogId"
        fun buildRoute(catalogId: String): String =
            "catalog_detail_view/${Uri.encode(catalogId)}"
    }

    object UserManagement : Route(route = "user")
    object Profile : Route(route = "profile")

    object MakingOrders : Route(route = "making_orders")
    object MakingOrdersSupplier : Route(route = "making_orders_supplier")
    object OrderDetail : Route(route = "order_detail")
    object OrderHistory : Route(route = "order_history")
    object ProductTransferHistory : Route(route = "transfers")
    object ProductExitHistory : Route(route = "exits")

    // Payments and Subscriptions Bounded Context
    object Plans : Route(route = "plans")
    object Subscriptions : Route(route = "subscriptions")
    object Congrats : Route(route = "congrats")
    object Failure : Route(route = "failure")
    object Pending: Route(route = "pending")
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
    const val SUBSCRIPTIONS = "subscriptions"
    const val ADMIN = "admin"
    const val PROFILE = "profile"
    const val LOGOUT = "logout"
}