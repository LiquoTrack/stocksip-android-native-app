package com.liquotrack.stocksip.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

/**
 * Composable function that sets up the navigation for the application.
 */
@Composable
fun AppNavigation() {

    // Create a NavController instance to handle navigation
    val navController = rememberNavController()

    // Define the navigation graph using NavHost
    NavHost(navController, startDestination = Route.Login.route) {

        // Define the route for Main screen
        composable (route = Route.Main.route) {

        }

        // Define the route for Login screen
        composable (route = Route.Login.route) {

        }

        // Define the route for Warehouses screen
        composable (
            route = Route.Warehouses.routeWithArgument,
            arguments = listOf(navArgument(Route.Warehouses.argument) {
                type = NavType.StringType
            })
        ) {

        }

        // Define the route for Products/Storage screen
        composable (
            route = Route.Products.routeWithArgument,
            arguments = listOf(navArgument(Route.Products.argument) {
                type = NavType.StringType
            })
        ) {

        }

        // Define the route for Product Detail screen
        composable (
            route = Route.ProductDetail.routeWithArgument,
            arguments = listOf(navArgument(Route.ProductDetail.argument) {
                type = NavType.StringType
            })
        ) {

        }

        // Define the route for Inventory (warehouse stock) screen
        composable (
            route = Route.Inventory.routeWithArgument,
            arguments = listOf(navArgument(Route.Inventory.argument) {
                type = NavType.StringType
            })
        ) {

        }


        // Define the route for Alerts screen
        composable (
            route = Route.Alerts.routeWithArgument,
            arguments = listOf(navArgument(Route.Alerts.argument) {
                type = NavType.StringType
            })
        ) {

        }

        // Define the route for Catalog screen
        composable (
            route = Route.Catalogs.routeWithArgument,
            arguments = listOf(navArgument(Route.Catalogs.argument) {
                type = NavType.StringType
            })
        ) {

        }

        // Define the route for Catalog Detail screen
        composable (
            route = Route.CatalogDetail.routeWithArgument,
            arguments = listOf(navArgument(Route.CatalogDetail.argument) {
                type = NavType.StringType
            })
        ) {

        }

        // Define the route for Care Guides screen
        composable (
            route = Route.CareGuides.routeWithArgument,
            arguments = listOf(navArgument(Route.CareGuides.argument) {
                type = NavType.StringType
            })
        ) {

        }

        // Define the route for User Management screen
        composable (
            route = Route.UserManagement.routeWithArgument,
            arguments = listOf(navArgument(Route.UserManagement.argument) {
                type = NavType.StringType
            })
        ) {

        }

        // Define the route for Profile screen
        composable (
            route = Route.Profile.routeWithArgument,
            arguments = listOf(navArgument(Route.Profile.argument) {
                type = NavType.StringType
            })
        ) {

        }

        // Define the route for Making Orders screen
        composable (
            route = Route.MakingOrders.routeWithArgument,
            arguments = listOf(navArgument(Route.MakingOrders.argument) {
                type = NavType.StringType
            })
        ) {

        }

        // Define the route for Order Detail screen
        composable (
            route = Route.OrderDetail.routeWithArgument,
            arguments = listOf(navArgument(Route.OrderDetail.argument) {
                type = NavType.StringType
            })
        ) {

        }

        // Define the route for Order History screen
        composable (
            route = Route.OrderHistory.routeWithArgument,
            arguments = listOf(navArgument(Route.OrderHistory.argument) {
                type = NavType.StringType
            })
        ) {

        }

        // Define the route for Product Transfer History screen
        composable (
            route = Route.ProductTransferHistory.routeWithArgument,
            arguments = listOf(navArgument(Route.ProductTransferHistory.argument) {
                type = NavType.StringType
            })
        ) {

        }

        // Define the route for Product Exit History
        composable (
            route = Route.ProductExitHistory.routeWithArgument,
            arguments = listOf(navArgument(Route.ProductExitHistory.argument) {
                type = NavType.StringType
            })
        ) {

        }

    }
}