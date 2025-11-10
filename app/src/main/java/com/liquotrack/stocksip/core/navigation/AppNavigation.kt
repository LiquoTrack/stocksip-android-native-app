package com.liquotrack.stocksip.core.navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.liquotrack.stocksip.features.authentication.adminpanel.presentation.AdminPanel
import com.liquotrack.stocksip.features.authentication.login.presentation.login.Login
import com.liquotrack.stocksip.features.authentication.register.presentation.register.RegisterAccount
import com.liquotrack.stocksip.features.authentication.login.presentation.register.RegisterUser
import com.liquotrack.stocksip.features.authentication.passwordrecover.presentation.ConfirmationCode
import com.liquotrack.stocksip.features.authentication.passwordrecover.presentation.RecoverPassword
import com.liquotrack.stocksip.features.inventorymanagement.careguides.presentation.CareGuideCreate
import com.liquotrack.stocksip.features.inventorymanagement.careguides.presentation.CareGuideEdit
import com.liquotrack.stocksip.features.inventorymanagement.careguides.presentation.CareGuides
import com.liquotrack.stocksip.features.home.presentation.home.HomeView
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse.WarehouseCreateAndEditView
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse.WarehouseView
import com.liquotrack.stocksip.features.paymentsandsubscriptions.plans.presentation.plan.ChoosePlanScreen
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.presentation.subscriptions.components.Congrats
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.presentation.subscriptions.components.Failure
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.presentation.subscriptions.components.Pending
import com.liquotrack.stocksip.features.profilemanagement.profile.presentation.Profile
import com.liquotrack.stocksip.features.ordermanagement.presentation.SalesOrdersView
import com.liquotrack.stocksip.features.ordermanagement.presentation.SupplierSalesOrdersView
import androidx.hilt.navigation.compose.hiltViewModel
import com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.presentation.account.AccountViewModel
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.presentation.subscriptions.AccountSubscriptionPlanView

/**
 * Main navigation graph of the app.
 * Includes authentication, home, warehouse, products, care guides, etc.
 */
@Composable
fun AppNavigation(startDestination: String = Route.Login.route) {

    val navController = rememberNavController()

    NavHost(navController, startDestination = startDestination) {

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
                },
                onNavigateToPlans = {
                    navController.navigate(Route.Plans.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                },
                onNavigateToPending = {
                    navController.navigate(Route.Pending.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                },
                onGoogleSignInSuccess = { email, fullName, accountExists ->
                    if (accountExists) {
                        navController.navigate(Route.Main.route) {
                            popUpTo(Route.Login.route) { inclusive = true }
                        }
                    } else {
                        val route = Route.RegisterAccount.buildRoute(
                            email = email,
                            fullName = fullName,
                            password = "GOOGLE_AUTH"
                        )
                        navController.navigate(route)
                    }
                }
            )
        }

        // REGISTER USER FLOW
        composable(route = Route.Register.route) {
            RegisterUser(
                onNavigateToAccountRegistration = { email, fullName, password ->
                    val route = "register_account/$email/$fullName/$password"
                    navController.navigate(route)
                }
            )
        }

        // REGISTER ACCOUNT AND BUSINESS FLOW
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
                        launchSingleTop = true
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
                },
                onLogout = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Profile
        composable(route = Route.Profile.route) {
            Profile(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Warehouses
        composable(route = Route.Warehouses.route) {
            WarehouseView(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Warehouse Create and Edit
        composable(
            route = "warehouse_create_edit/{warehouseId}",
            arguments = listOf(navArgument("warehouseId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val warehouseId = backStackEntry.arguments?.getString("warehouseId")
            WarehouseCreateAndEditView(
                warehouseId = warehouseId ?: "new",
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // User Management
        composable(route = Route.UserManagement.route) {
            AdminPanel(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // Products Storage
        composable(route = Route.Products.route) {
        }

        // Care Guides
        composable(route = Route.CareGuides.route) {
            CareGuides(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Care Guide Create and Edit
        composable(route = Route.CareGuideCreate.route) {
            CareGuideCreate(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Care Guide Edit with argument
        composable(
            route = Route.CareGuideEdit.routeWithArguments,
            arguments = listOf(
                navArgument(Route.CareGuideEdit.careGuideIdArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val careGuideId = backStackEntry.arguments?.getString(Route.CareGuideEdit.careGuideIdArg).orEmpty()
            CareGuideEdit(
                careGuideId = careGuideId,
                onNavigateBack = { navController.popBackStack() },
                onDeleted = {
                    navController.popBackStack(Route.CareGuides.route, false)
                }
            )
        }

        composable(route = Route.Catalogs.route) {
        }

        // Making Orders
        composable(route = Route.MakingOrders.route) {
            val accountViewModel: AccountViewModel = hiltViewModel()
            val role by accountViewModel.accountRole.collectAsState()
            LaunchedEffect(role) {
                if (role == null) {
                    accountViewModel.loadAccountRoleFromStorage()
                }
            }
            val roleNormalized = role?.trim()?.lowercase()
            if (roleNormalized == "supplier") {
                SupplierSalesOrdersView(
                    onNavigate = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                        }
                    },
                    onChangeStatus = { },
                    onLogout = {
                        navController.navigate(Route.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            } else {
                SalesOrdersView(
                    onNavigate = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                        }
                    },
                    onNewClick = { },
                    onLogout = {
                        navController.navigate(Route.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(route = Route.MakingOrdersSupplier.route) {
            SupplierSalesOrdersView(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                },
                onChangeStatus = { },
                onLogout = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Route.Plans.route) {
            ChoosePlanScreen(
                onContinue = { selectedPlan ->
                    navController.navigate(Route.Main.route) {
                        popUpTo(Route.Plans.route) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Route.Congrats.route) {
            Congrats(
                onNavigateToHome = {
                    navController.navigate(Route.Main.route) {
                        popUpTo(Route.Congrats.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Route.Failure.route) {
            Failure(
                onNavigateToHome = {
                    navController.navigate(Route.Main.route) {
                        popUpTo(Route.Failure.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Route.Pending.route) {
            Pending(
                onNavigateToHome = {
                    navController.navigate(Route.Main.route) {
                        popUpTo(Route.Pending.route) { inclusive = true }
                    }
                }
            )
        }

        // Subscriptions
        composable(route = Route.Subscriptions.route) {
            AccountSubscriptionPlanView(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}