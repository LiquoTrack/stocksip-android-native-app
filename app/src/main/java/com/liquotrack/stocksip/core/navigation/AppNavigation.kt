package com.liquotrack.stocksip.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.liquotrack.stocksip.features.authentication.login.presentation.login.Login
import com.liquotrack.stocksip.features.profilemanagement.profile.presentation.Profile

/**
 * Composable function that sets up the navigation for the application.
 */
@Composable
fun AppNavigation() {

    // Create a NavController instance to handle navigation
    val navController = rememberNavController()

    // Helper function to navigate between screens
    val navigateToRoute: (String) -> Unit = { route ->
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    // Define the navigation graph using NavHost
    NavHost(navController, startDestination = Route.Login.route) {

        // Login screen
        composable(route = Route.Login.route) {
            Login(
                onNavigateToRegister = {
                    // navController.navigate(Route.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Route.Main.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Main screen (Home/Dashboard)
        composable(route = Route.Main.route) {
            // TODO: Implementar HomeScreen con Drawer
            // HomeScreen(
            //     username = currentUser.name,
            //     onNavigate = navigateToRoute
            // )
        }

        // Warehouses screen
        composable(
            route = Route.Warehouses.routeWithArgument,
            arguments = listOf(navArgument(Route.Warehouses.argument) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString(Route.Warehouses.argument)
            // TODO: Implement WarehousesScreen
            // WarehousesScreen(
            //     accountId = accountId,
            //     username = currentUser.name,
            //     onNavigate = navigateToRoute
            // )
        }

        // Products/Storage screen
        composable(
            route = Route.Products.routeWithArgument,
            arguments = listOf(navArgument(Route.Products.argument) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString(Route.Products.argument)
            // TODO: Implement ProductsScreen
        }

        // Product Detail screen
        composable(
            route = Route.ProductDetail.routeWithArgument,
            arguments = listOf(navArgument(Route.ProductDetail.argument) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString(Route.ProductDetail.argument)
            // TODO: Implement ProductDetailScreen (without Drawer, with back button)
        }

        // Inventory (warehouse stock) screen
        composable(
            route = Route.Inventory.routeWithArgument,
            arguments = listOf(navArgument(Route.Inventory.argument) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val warehouseId = backStackEntry.arguments?.getString(Route.Inventory.argument)
            // TODO: Implement InventoryScreen
        }

        // Alerts screen
        composable(
            route = Route.Alerts.routeWithArgument,
            arguments = listOf(navArgument(Route.Alerts.argument) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString(Route.Alerts.argument)
            // TODO: Implement AlertsScreen
        }

        // Catalog screen
        composable(
            route = Route.Catalogs.routeWithArgument,
            arguments = listOf(navArgument(Route.Catalogs.argument) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString(Route.Catalogs.argument)
            // TODO: Implement CatalogsScreen
        }

        // Catalog Detail screen
        composable(
            route = Route.CatalogDetail.routeWithArgument,
            arguments = listOf(navArgument(Route.CatalogDetail.argument) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val catalogId = backStackEntry.arguments?.getString(Route.CatalogDetail.argument)
            // TODO: Implement CatalogDetailScreen (without Drawer, with back button)
        }

        // Care Guides screen
        composable(
            route = Route.CareGuides.routeWithArgument,
            arguments = listOf(navArgument(Route.CareGuides.argument) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString(Route.CareGuides.argument)
            // TODO: Implement CareGuidesScreen
        }

        // User Management screen (Admin Panel)
        composable(
            route = Route.UserManagement.routeWithArgument,
            arguments = listOf(navArgument(Route.UserManagement.argument) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString(Route.UserManagement.argument)
            // TODO: Implement UserManagementScreen
        }

        // Profile screen
        composable(
            route = Route.Profile.routeWithArgument,
            arguments = listOf(navArgument(Route.Profile.argument) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString(Route.Profile.argument) ?: ""

            Profile(
                username = "John Doe", // TODO: Replace with actual username
                onNavigate = { route ->
                    navController.navigateFromDrawer(route, userId)
                }
            )
        }

        // Plans screen
        composable(
            route = Route.Plans.routeWithArgument,
            arguments = listOf(navArgument(Route.Plans.argument) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString(Route.Plans.argument) ?: ""
            // TODO: Implement PlansScreen
            // PlansScreen(
            //     username = "John Doe",
            //     onNavigate = { route ->
            //         navController.navigateFromDrawer(route, userId)
            //     }
            // )
        }

        // Making Orders screen
        composable(
            route = Route.MakingOrders.routeWithArgument,
            arguments = listOf(navArgument(Route.MakingOrders.argument) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString(Route.MakingOrders.argument)
            // TODO: Implement MakingOrdersScreen
        }

        // Order Detail screen
        composable(
            route = Route.OrderDetail.routeWithArgument,
            arguments = listOf(navArgument(Route.OrderDetail.argument) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString(Route.OrderDetail.argument)
            // TODO: Implement OrderDetailScreen (without Drawer, with back button)
        }

        // Order History screen
        composable(
            route = Route.OrderHistory.routeWithArgument,
            arguments = listOf(navArgument(Route.OrderHistory.argument) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString(Route.OrderHistory.argument)
            // TODO: Implement OrderHistoryScreen
        }

        // Product Transfer History screen
        composable(
            route = Route.ProductTransferHistory.routeWithArgument,
            arguments = listOf(navArgument(Route.ProductTransferHistory.argument) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString(Route.ProductTransferHistory.argument)
            // TODO: Implement ProductTransferHistoryScreen
        }

        // Product Exit History screen
        composable(
            route = Route.ProductExitHistory.routeWithArgument,
            arguments = listOf(navArgument(Route.ProductExitHistory.argument) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString(Route.ProductExitHistory.argument)
            // TODO: Implement ProductExitHistoryScreen
        }
    }
}