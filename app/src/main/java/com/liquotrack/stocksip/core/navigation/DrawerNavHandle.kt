package com.liquotrack.stocksip.core.navigation

import androidx.navigation.NavController

/**
 * Helper class to handle navigation from the NavigationDrawer
 * Converts simplified drawer routes to actual Route objects
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
                }
            }

            DrawerRoutes.WAREHOUSE -> {
                navController.navigate(NavigationHelper.getWarehouseRoute(userId)) {
                    launchSingleTop = true
                }
            }

            DrawerRoutes.CARE_GUIDES -> {
                navController.navigate(NavigationHelper.getCareGuidesRoute(userId)) {
                    launchSingleTop = true
                }
            }

            DrawerRoutes.ORDERS -> {
                navController.navigate(NavigationHelper.getMakingOrdersRoute(userId)) {
                    launchSingleTop = true
                }
            }

            DrawerRoutes.PRODUCTS -> {
                navController.navigate(NavigationHelper.getProductsRoute(userId)) {
                    launchSingleTop = true
                }
            }

            DrawerRoutes.CATALOG -> {
                navController.navigate(NavigationHelper.getCatalogsRoute(userId)) {
                    launchSingleTop = true
                }
            }

            DrawerRoutes.PLANS -> {
                navController.navigate(NavigationHelper.getPlansRoute(userId)) {
                    launchSingleTop = true
                }
            }

            DrawerRoutes.ADMIN -> {
                navController.navigate(NavigationHelper.getUserManagementRoute(userId)) {
                    launchSingleTop = true
                }
            }

            DrawerRoutes.PROFILE -> {
                navController.navigate(NavigationHelper.getProfileRoute(userId)) {
                    launchSingleTop = true
                }
            }

            "logout" -> {
                navController.navigate(Route.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
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