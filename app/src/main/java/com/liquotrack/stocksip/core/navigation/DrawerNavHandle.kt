package com.liquotrack.stocksip.core.navigation

import androidx.navigation.NavController

/**
 * Helper class to handle navigation from the NavigationDrawer
 * Converts simplified drawer routes to actual Route objects with userId argument
 */
class DrawerNavigationHandler(
    private val navController: NavController,
    private val userId: String
) {

    fun navigate(drawerRoute: String) {
        when (drawerRoute) {
            DrawerRoutes.HOME -> {
                navController.navigate(Route.Main.route) {
                    launchSingleTop = true
                    popUpTo(Route.Main.route) { inclusive = true }
                }
            }

            DrawerRoutes.WAREHOUSE -> {
                val route = "warehouses/$userId"
                navController.navigate(route) {
                    launchSingleTop = true
                }
            }

            DrawerRoutes.CARE_GUIDES -> {
                val route = "care_guide/$userId"
                navController.navigate(route) {
                    launchSingleTop = true
                }
            }

            DrawerRoutes.ORDERS -> {
                val route = "making_orders/$userId"
                navController.navigate(route) {
                    launchSingleTop = true
                }
            }

            DrawerRoutes.PRODUCTS -> {
                val route = "products_storage/$userId"
                navController.navigate(route) {
                    launchSingleTop = true
                }
            }

            DrawerRoutes.CATALOG -> {
                val route = "catalogs/$userId"
                navController.navigate(route) {
                    launchSingleTop = true
                }
            }

            DrawerRoutes.PLANS -> {
                val route = "plans/$userId"
                navController.navigate(route) {
                    launchSingleTop = true
                }
            }

            DrawerRoutes.ADMIN -> {
                val route = "user/$userId"
                navController.navigate(route) {
                    launchSingleTop = true
                }
            }

            DrawerRoutes.PROFILE -> {
                val route = "profile/$userId"
                navController.navigate(route) {
                    launchSingleTop = true
                }
            }

            "logout" -> {
                navController.navigate(Route.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            }

            else -> {
                println("DrawerNavigationHandler: Unknown route - $drawerRoute")
            }
        }
    }
}

/**
 * Extension function to simplify drawer navigation
 */
fun NavController.navigateFromDrawer(route: String, userId: String) {
    val handler = DrawerNavigationHandler(this, userId)
    handler.navigate(route)
}