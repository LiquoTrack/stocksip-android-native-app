package com.liquotrack.stocksip.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.liquotrack.stocksip.features.adminpanel.presentation.AdminPanel
import com.liquotrack.stocksip.features.authentication.login.presentation.login.Login
import com.liquotrack.stocksip.features.authentication.register.presentation.register.RegisterAccount
import com.liquotrack.stocksip.features.authentication.register.presentation.register.RegisterUser
import com.liquotrack.stocksip.features.profilemanagement.profile.presentation.Profile

/**
 * Composable function that sets up the navigation for the application.
 * Simplified version for testing without authentication requirements.
 */
@Composable
fun AppNavigation() {

    // Create a NavController instance to handle navigation
    val navController = rememberNavController()

    // Define the navigation graph using NavHost
    NavHost(navController, startDestination = Route.Profile.route) {

        // Login screen
        composable(route = Route.Login.route) {
            Login(
                onNavigateToRegister = {
                    navController.navigate(Route.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Route.Main.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Register screen (User Info)
        composable(route = Route.Register.route) {
            RegisterUser(
                onNavigateToAccountRegistration = { email, fullName, password ->
                    val route = "register_account/$email/$fullName/$password"
                    navController.navigate(route)
                }
            )
        }

        // Register Account screen (Account Info)
        composable(
            route = Route.RegisterAccount.routeWithArguments,
            arguments = listOf(
                navArgument(Route.RegisterAccount.emailArg) { type = NavType.StringType },
                navArgument(Route.RegisterAccount.fullNameArg) { type = NavType.StringType },
                navArgument(Route.RegisterAccount.passwordArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString(Route.RegisterAccount.emailArg) ?: ""
            val fullName = backStackEntry.arguments?.getString(Route.RegisterAccount.fullNameArg) ?: ""
            val password = backStackEntry.arguments?.getString(Route.RegisterAccount.passwordArg) ?: ""

            RegisterAccount(
                email = email,
                username = fullName,
                password = password,
                onRegistrationSuccess = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Main screen (Home/Dashboard)
        composable(route = Route.Main.route) {
            // TODO: Implement HomeScreen
        }

        // Warehouses screen
        composable(route = Route.Warehouses.route) {
            // TODO: Implement WarehousesScreen
        }

        // Products/Storage screen
        composable(route = Route.Products.route) {
            // TODO: Implement ProductsScreen
        }

        // Alerts screen
        composable(route = Route.Alerts.route) {
            // TODO: Implement AlertsScreen
        }

        // Catalog screen
        composable(route = Route.Catalogs.route) {
            // TODO: Implement CatalogsScreen
        }

        // Care Guides screen
        composable(route = Route.CareGuides.route) {
            // TODO: Implement CareGuidesScreen
        }

        // User Management screen (Admin Panel)
        composable(route = Route.UserManagement.route) {
            AdminPanel(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // Profile screen
        composable(route = Route.Profile.route) {
            Profile(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // Plans screen
        composable(route = Route.Plans.route) {
            // TODO: Implement PlansScreen
        }

        // Making Orders screen
        composable(route = Route.MakingOrders.route) {
            // TODO: Implement MakingOrdersScreen
        }

        // Order History screen
        composable(route = Route.OrderHistory.route) {
            // TODO: Implement OrderHistoryScreen
        }

        // Product Transfer History screen
        composable(route = Route.ProductTransferHistory.route) {
            // TODO: Implement ProductTransferHistoryScreen
        }

        // Product Exit History screen
        composable(route = Route.ProductExitHistory.route) {
            // TODO: Implement ProductExitHistoryScreen
        }
    }
}