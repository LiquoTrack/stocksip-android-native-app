package com.liquotrack.stocksip.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.liquotrack.stocksip.features.adminpanel.presentation.AdminPanel
import com.liquotrack.stocksip.features.authentication.login.presentation.login.Login
import com.liquotrack.stocksip.features.authentication.passwordrecover.presentation.ConfirmationCode
import com.liquotrack.stocksip.features.authentication.passwordrecover.presentation.RecoverPassword
import com.liquotrack.stocksip.features.authentication.register.presentation.register.RegisterAccount
import com.liquotrack.stocksip.features.authentication.register.presentation.register.RegisterUser
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse.WarehouseView
import com.liquotrack.stocksip.features.profilemanagement.profile.presentation.Profile

/**
 * Composable function that sets up the navigation graph for the app.
 * This version includes authentication, profile, warehouse, and admin panel routes.
 */
@Composable
fun AppNavigation() {

    // NavController instance to handle navigation actions
    val navController = rememberNavController()

    // Define all navigation destinations using NavHost
    NavHost(navController, startDestination = Route.Profile.route) {

        // Login screen
        composable(route = Route.Login.route) {
            Login(
                onNavigateToRegister = {
                    navController.navigate(Route.Register.route)
                },
                onNavigateToRecovery = {
                    navController.navigate(Route.PasswordRecovery.route)
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

        // Password Recovery screen
        composable(route = Route.PasswordRecovery.route) {
            RecoverPassword(
                onNavigateToConfirmation = { email ->
                    val route = "confirmation_code/$email"
                    navController.navigate(route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Confirmation Code screen
        composable(
            route = Route.ConfirmationCode.routeWithArguments,
            arguments = listOf(
                navArgument(Route.ConfirmationCode.emailArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString(Route.ConfirmationCode.emailArg) ?: ""

            ConfirmationCode(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(0) { inclusive = true }
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

        // Warehouse screen
        composable(route = Route.Warehouses.route) {
            WarehouseView(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // Admin Panel screen
        composable(route = Route.UserManagement.route) {
            AdminPanel(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // Main and placeholder routes (to be implemented)
        composable(route = Route.Main.route) { /* TODO: Implement MainScreen */ }
        composable(route = Route.Products.route) { /* TODO: Implement ProductsScreen */ }
        composable(route = Route.Alerts.route) { /* TODO: Implement AlertsScreen */ }
        composable(route = Route.Catalogs.route) { /* TODO: Implement CatalogsScreen */ }
        composable(route = Route.CareGuides.route) { /* TODO: Implement CareGuidesScreen */ }
        composable(route = Route.Plans.route) { /* TODO: Implement PlansScreen */ }
        composable(route = Route.MakingOrders.route) { /* TODO: Implement MakingOrdersScreen */ }
        composable(route = Route.OrderHistory.route) { /* TODO: Implement OrderHistoryScreen */ }
        composable(route = Route.ProductTransferHistory.route) { /* TODO: Implement ProductTransferHistoryScreen */ }
        composable(route = Route.ProductExitHistory.route) { /* TODO: Implement ProductExitHistoryScreen */ }
    }
}