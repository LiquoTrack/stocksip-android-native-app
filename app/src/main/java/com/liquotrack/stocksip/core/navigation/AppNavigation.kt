package com.liquotrack.stocksip.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.liquotrack.stocksip.features.adminpanel.presentation.AdminPanel
import com.liquotrack.stocksip.features.authentication.login.presentation.login.Login
import com.liquotrack.stocksip.features.authentication.login.presentation.register.RegisterAccount
import com.liquotrack.stocksip.features.authentication.login.presentation.register.RegisterUser
import com.liquotrack.stocksip.features.authentication.passwordrecover.presentation.ConfirmationCode
import com.liquotrack.stocksip.features.authentication.passwordrecover.presentation.RecoverPassword
import com.liquotrack.stocksip.features.careguides.presentation.CareGuideCreate
import com.liquotrack.stocksip.features.careguides.presentation.CareGuides
import com.liquotrack.stocksip.features.home.presentation.home.HomeView
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse.WarehouseCreateAndEditView
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse.WarehouseView
import com.liquotrack.stocksip.features.profilemanagement.profile.presentation.Profile

/**
 * Main navigation graph of the app.
 * Includes authentication, home, warehouse, products, care guides, etc.
 */
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(navController, startDestination = Route.Login.route) {

        // AUTHENTICATION FLOW
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

        composable(route = Route.Register.route) {
            RegisterUser(
                onNavigateToAccountRegistration = { email, fullName, password ->
                    val route = "register_account/$email/$fullName/$password"
                    navController.navigate(route)
                }
            )
        }

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

        composable(route = Route.PasswordRecovery.route) {
            RecoverPassword(
                onNavigateToConfirmation = { email ->
                    val route = "confirmation_code/$email"
                    navController.navigate(route)
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Route.ConfirmationCode.routeWithArguments,
            arguments = listOf(
                navArgument(Route.ConfirmationCode.emailArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString(Route.ConfirmationCode.emailArg) ?: ""
            ConfirmationCode(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // MAIN FLOW
        composable(route = Route.Main.route) {
            HomeView(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = Route.Profile.route) {
            Profile(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = Route.Warehouses.route) {
            WarehouseView(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Route.WarehouseCreateEdit.routeWithArgs,
            arguments = listOf(
                navArgument("warehouseId") {
                    type = NavType.StringType
                    defaultValue = "new"
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val warehouseId = backStackEntry.arguments?.getString("warehouseId")

            WarehouseCreateAndEditView(
                warehouseId = warehouseId ?: "new",
                warehouse = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }


        composable(route = Route.UserManagement.route) {
            AdminPanel(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = Route.Products.route) {

        }

        composable(route = Route.CareGuides.route) {
            CareGuides(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = Route.CareGuideCreate.route) {
            CareGuideCreate(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Route.Catalogs.route) {

        }

        composable(route = Route.MakingOrders.route) {

        }

        composable(route = Route.Plans.route) {

        }
    }
}