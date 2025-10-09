package com.liquotrack.stocksip.core.navigation

import androidx.navigation.NavController

/**
 * Helper class to handle navigation from the NavigationDrawer
 * Simplified version - no user authentication required
 */
class DrawerNavigationHandler(
    private val navController: NavController
) {

    fun navigate(drawerRoute: String) {
        println("🔍 DrawerNavigationHandler: Trying to navigate to: $drawerRoute")

        val route = when (drawerRoute) {
            "home", DrawerRoutes.HOME -> {
                println("✅ Navigating to HOME")
                Route.Main.route
            }
            "warehouse", DrawerRoutes.WAREHOUSE -> {
                println("✅ Navigating to WAREHOUSE")
                Route.Warehouses.route
            }
            "care_guides", DrawerRoutes.CARE_GUIDES -> {
                println("✅ Navigating to CARE_GUIDES")
                Route.CareGuides.route
            }
            "orders", DrawerRoutes.ORDERS -> {
                println("✅ Navigating to ORDERS")
                Route.MakingOrders.route
            }
            "products", DrawerRoutes.PRODUCTS -> {
                println("✅ Navigating to PRODUCTS")
                Route.Products.route
            }
            "catalog", DrawerRoutes.CATALOG -> {
                println("✅ Navigating to CATALOG")
                Route.Catalogs.route
            }
            "plans", DrawerRoutes.PLANS -> {
                println("✅ Navigating to PLANS")
                Route.Plans.route
            }
            "admin", DrawerRoutes.ADMIN -> {
                println("✅ Navigating to ADMIN")
                Route.UserManagement.route
            }
            "profile", DrawerRoutes.PROFILE -> {
                println("✅ Navigating to PROFILE")
                Route.Profile.route
            }
            "logout" -> {
                println("✅ Navigating to LOGOUT")
                navController.navigate(Route.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
                return
            }
            else -> {
                println("❌ Unknown route: $drawerRoute")
                return
            }
        }

        navController.navigate(route) {
            launchSingleTop = true
        }
    }
}

/**
 * Extension function to simplify drawer navigation
 * No userId required for testing
 */
fun NavController.navigateFromDrawer(route: String) {
    val handler = DrawerNavigationHandler(this)
    handler.navigate(route)
}