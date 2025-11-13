package com.liquotrack.stocksip.core.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.liquotrack.stocksip.features.authentication.adminpanel.presentation.AdminPanel
import com.liquotrack.stocksip.features.authentication.login.presentation.login.Login
import com.liquotrack.stocksip.features.authentication.register.presentation.register.RegisterAccount
import com.liquotrack.stocksip.features.authentication.register.presentation.register.RegisterUser
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
import com.liquotrack.stocksip.features.ordermanagement.presentation.SupplierSalesOrdersView
import androidx.hilt.navigation.compose.hiltViewModel
import com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.productcreateoredit.StorageCreateOrEditView
import com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.storage.StorageView
import com.liquotrack.stocksip.features.authentication.passwordrecover.presentation.UpdatePasswordView
import com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventory.InventoryView
import com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventoryaddition.InventoryAdditionView
import com.liquotrack.stocksip.features.inventorymanagement.storage.presentation.productdetail.ProductDetailView
import com.liquotrack.stocksip.features.ordermanagement.presentation.PurchaseOrdersView
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.presentation.PurchaseOrdersViewModel
import com.liquotrack.stocksip.features.paymentsandsubscriptions.accounts.presentation.account.AccountViewModel
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.presentation.AddressListScreen
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.presentation.AddressViewModel
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.presentation.subscriptions.AccountSubscriptionPlanView
import com.liquotrack.stocksip.features.procurementordering.purchaseorders.presentation.cart.CartScreen
import com.liquotrack.stocksip.features.procurementordering.purchaseorders.presentation.cart.CartViewModel
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation.CatalogCreateAndEditScreen
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation.CatalogDetailScreen
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation.CatalogListScreen
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation.storeownercatalogs.presentation.CatalogDetailViewScreen
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation.storeownercatalogs.presentation.CatalogItemDetailScreen
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation.storeownercatalogs.presentation.CatalogItemDetailViewModel
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation.storeownercatalogs.presentation.SupplierCatalogListScreen
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation.storeownercatalogs.presentation.SupplierSearchScreen
import com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventorysubtrack.InventorySubtrackView
import java.net.URLEncoder


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.liquotrack.stocksip.features.alerts.presentation.alerts.AlertsViewModel
import com.liquotrack.stocksip.features.alerts.presentation.alerts.components.AlertsOverlay


/**
 * Main navigation graph of the app.
 * Includes authentication, home, warehouse, products, care guides, etc.
 */
@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AppNavigation(startDestination: String = Route.Login.route) {

    val navController = rememberNavController()

    NavHost(navController, startDestination = startDestination) {

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
                onGoogleSignInSuccess = { _, _, _ ->
                    navController.navigate(Route.Plans.route) {
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
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = Route.PasswordRecovery.route) {
            RecoverPassword(
                onNavigateToConfirmation = { email ->
                    val encodedEmail = URLEncoder.encode(email, "UTF-8")
                    navController.navigate("confirmation_code/$encodedEmail")
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
            val email = backStackEntry.arguments?.getString("email")
                ?.let { java.net.URLDecoder.decode(it, "UTF-8") } ?: ""

            ConfirmationCode(
                email = email,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToUpdatePassword = {
                    val encodedEmail = URLEncoder.encode(email, "UTF-8")
                    navController.navigate("update_password/$encodedEmail")
                }
            )
        }

        composable(
            route = Route.UpdatePassword.routeWithArguments,
            arguments = listOf(
                navArgument(Route.UpdatePassword.emailArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString(Route.UpdatePassword.emailArg)
                ?.let { java.net.URLDecoder.decode(it, "UTF-8") } ?: ""

            UpdatePasswordView(
                email = email,
                onNavigateToLogin = {
                    navController.navigate(Route.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Route.Main.route) {

            val alertsViewModel: AlertsViewModel = hiltViewModel()
            val alertsToShow by alertsViewModel.alertsToShowInOverlay.collectAsStateWithLifecycle()

            Box(modifier = Modifier.fillMaxSize()) {

                // 3. Tu HomeView (Capa inferior)
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

                if (alertsToShow.isNotEmpty()) {
                    AlertsOverlay(
                        alerts = alertsToShow,
                        onDismiss = {
                            alertsViewModel.dismissOverlay()
                        }
                    )
                }
            }
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

        // Inventories
        composable(
            route = Route.Inventory.routeWithArgs,
            arguments = listOf(
                navArgument(Route.Inventory.warehouseIdArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val warehouseId = backStackEntry.arguments?.getString(Route.Inventory.warehouseIdArg)
            if (warehouseId != null) {
                InventoryView(
                    warehouseId = warehouseId,
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
            } else {
                // Handle null warehouseId case and navigates back
                navController.popBackStack()
            }
        }

        // Inventory Addition Form
        composable(
            route = Route.InventoryAddition.routeWithArgs,
            arguments = listOf(
                navArgument(Route.InventoryAddition.warehouseIdArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val warehouseId = backStackEntry.arguments?.getString(Route.InventoryAddition.warehouseIdArg)
            if (warehouseId == null) {
                // Handle null warehouseId case and navigates back
                navController.popBackStack()
            } else {
                InventoryAdditionView(
                    warehouseId = warehouseId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        // Inventory Subtrack Form
        composable(
            route = Route.InventorySubtrack.routeWithArgs,
            arguments = listOf(
                navArgument(Route.InventorySubtrack.warehouseIdArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val warehouseId = backStackEntry.arguments?.getString(Route.InventorySubtrack.warehouseIdArg)
            if (warehouseId == null) {
                // Handle null warehouseId case and navigates back
                navController.popBackStack()
            } else {
                InventorySubtrackView(
                    warehouseId = warehouseId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
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

        // Products Storage
        composable(route = Route.Products.route) {
            StorageView(
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

        // Product Detail
        composable(
            route = Route.ProductDetail.routeWithArgs,
            arguments = listOf(navArgument(Route.ProductDetail.productIdArg) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString(Route.ProductDetail.productIdArg)
            if (productId == null) {
                // Handle null productId case and navigates back
                navController.popBackStack()
            } else {
                ProductDetailView(
                    productId = productId,
                    onNavigate = { route ->
                        navController.navigate(route) { launchSingleTop = true }
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        // Product Create And Edit
        composable(
            route = Route.ProductCreateEdit.routeWithArgs,
            arguments = listOf(navArgument(Route.ProductCreateEdit.productIdArg) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString(Route.ProductCreateEdit.productIdArg)
            StorageCreateOrEditView(
                productId = productId ?: "new",
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

        // Catalogs
        composable(route = Route.Catalogs.route) {
            val accountViewModel: AccountViewModel = hiltViewModel()
            val role by accountViewModel.accountRole.collectAsState()

            LaunchedEffect(role) {
                if (role == null) accountViewModel.loadAccountRoleFromStorage()
            }

            val roleNormalized = role?.trim()?.lowercase()

            if (roleNormalized == "supplier") {
                CatalogListScreen(
                    onNavigate = { route -> navController.navigate(route) { launchSingleTop = true } },
                    onMenuClick = { },
                    onCreateCatalog = { navController.navigate(Route.CatalogCreateEdit.buildRoute("new")) },
                    onCatalogClick = { catalogId ->
                        navController.navigate(Route.CatalogDetail.buildRoute(catalogId))
                    },
                    onLogout = {
                        navController.navigate(Route.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            else if (roleNormalized == "liquorstoreowner") {
                SupplierSearchScreen(
                    onNavigate = { route -> navController.navigate(route) { launchSingleTop = true } },
                    onMenuClick = {  },
                    onSupplierSelected = { supplierId ->
                        navController.navigate(Route.SupplierCatalogList.buildRoute(supplierId))
                    },
                    onLogout = {
                        navController.navigate(Route.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            else {
                CatalogListScreen(
                    onNavigate = { route -> navController.navigate(route) { launchSingleTop = true } },
                    onMenuClick = {  },
                    onCreateCatalog = { navController.navigate(Route.CatalogCreateEdit.buildRoute("new")) },
                    onCatalogClick = { catalogId ->
                        navController.navigate(Route.CatalogDetail.buildRoute(catalogId))
                    },
                    onLogout = {
                        navController.navigate(Route.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(
            route = "supplier_catalog_list/{supplierId}",
            arguments = listOf(navArgument("supplierId") { type = NavType.StringType })
        ) { backStackEntry ->
            val supplierId = backStackEntry.arguments?.getString("supplierId") ?: ""

            SupplierCatalogListScreen(
                supplierId = supplierId,
                onBackClick = { navController.popBackStack() },
                onCatalogSelected = { catalogId ->
                    navController.navigate("catalog_detail/$catalogId")
                }
            )
        }

        composable(
            route = "catalog_detail/{catalogId}",
            arguments = listOf(navArgument("catalogId") { type = NavType.StringType })
        ) { backStackEntry ->
            val catalogId = backStackEntry.arguments?.getString("catalogId") ?: ""
            val accountViewModel: AccountViewModel = hiltViewModel()
            val role by accountViewModel.accountRole.collectAsState()

            LaunchedEffect(role) {
                if (role == null) accountViewModel.loadAccountRoleFromStorage()
            }

            val roleNormalized = role?.trim()?.lowercase()

            when (roleNormalized) {
                "supplier" -> {
                    CatalogDetailScreen(
                        catalogId = catalogId,
                        onBack = { navController.popBackStack() },
                        onEdit = {
                            navController.navigate("catalog_edit/$catalogId")
                        }
                    )
                }

                "liquorstoreowner" -> {
                    CatalogDetailViewScreen(
                        catalogId = catalogId,
                        onBackClick = { navController.popBackStack() },
                        onProductClick = { item ->
                            navController.navigate("catalogItemDetail/$catalogId/${item.productId}")
                        }
                    )
                }


                else -> {
                    CatalogDetailViewScreen(
                        catalogId = catalogId,
                        onBackClick = { navController.popBackStack() },
                        onProductClick = { item ->
                            navController.navigate("catalogItemDetail/$catalogId/${item.productId}")
                        }
                    )
                }
            }
        }

        composable(
            route = Route.CatalogCreateEdit.routeWithArguments,
            arguments = listOf(navArgument(Route.CatalogCreateEdit.catalogIdArg) { type = NavType.StringType })
        ) { backStackEntry ->
            val catalogId = backStackEntry.arguments?.getString(Route.CatalogCreateEdit.catalogIdArg)
            val isEditMode = catalogId != null && catalogId != "new"

            CatalogCreateAndEditScreen(
                isEditMode = isEditMode,
                catalogId = catalogId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "catalog_edit/{catalogId}",
            arguments = listOf(navArgument("catalogId") { type = NavType.StringType })
        ) { backStackEntry ->
            val catalogId = backStackEntry.arguments?.getString("catalogId")
            val isEditMode = catalogId != null

            CatalogCreateAndEditScreen(
                isEditMode = isEditMode,
                catalogId = catalogId,
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            "catalogItemDetail/{catalogId}/{productId}",
            arguments = listOf(
                navArgument("catalogId") { type = NavType.StringType },
                navArgument("productId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val viewModel: CatalogItemDetailViewModel = hiltViewModel()
            CatalogItemDetailScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onNavigateToCart = {
                    val catalogId = viewModel.catalogId
                    navController.navigate("cart/$catalogId")
                }
            )
        }

        composable(
            route = "cart/{catalogId}",
            arguments = listOf(navArgument("catalogId") { type = NavType.StringType })
        ) { backStackEntry ->
            val catalogId = backStackEntry.arguments?.getString("catalogId") ?: ""

            val cartBackStackEntry = remember(backStackEntry) {
                navController.getBackStackEntry("cart/{catalogId}")
            }

            val cartViewModel: CartViewModel = hiltViewModel(cartBackStackEntry)

            LaunchedEffect(catalogId) {
                cartViewModel.setCatalogId(catalogId)
            }

            CartScreen(
                viewModel = cartViewModel,
                catalogId = catalogId,
                onBackClick = { navController.popBackStack() },
                onNextClick = {
                    navController.navigate(Route.Addresses.route) {
                        launchSingleTop = true
                        restoreState = false
                    }
                }
            )
        }

        composable(Route.Addresses.route) {
            val cartBackStackEntry = remember {
                navController.getBackStackEntry("cart/{catalogId}")
            }
            val cartViewModel: CartViewModel = hiltViewModel(cartBackStackEntry)

            val addressViewModel: AddressViewModel = hiltViewModel()
            val purchaseOrdersViewModel: PurchaseOrdersViewModel = hiltViewModel()

            AddressListScreen(
                addressViewModel = addressViewModel,
                purchaseOrdersViewModel = purchaseOrdersViewModel,
                cartViewModel = cartViewModel,
                onBackClick = { navController.popBackStack() },
                onOrderCreated = {
                    navController.navigate(Route.MakingOrders.route) {
                        popUpTo("cart/{catalogId}") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }


        // Making Orders
        composable(Route.MakingOrders.route) {
            val accountViewModel: AccountViewModel = hiltViewModel()
            val role by accountViewModel.accountRole.collectAsState()

            LaunchedEffect(role) {
                if (role == null) accountViewModel.loadAccountRoleFromStorage()
            }

            val roleNormalized = role?.trim()?.lowercase()
            if (roleNormalized == "supplier") {
                SupplierSalesOrdersView(
                    onNavigate = { route -> navController.navigate(route) { launchSingleTop = true } },
                    onLogout = {
                        navController.navigate(Route.Login.route) { popUpTo(0) { inclusive = true } }
                    }
                )
            } else {
                PurchaseOrdersView(
                    onNavigate = { route -> navController.navigate(route) { launchSingleTop = true } },
                    onLogout = { navController.navigate(Route.Login.route) { popUpTo(0) { inclusive = true } } },
                    navToCreate = { navController.navigate(Route.Catalogs.route) }
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