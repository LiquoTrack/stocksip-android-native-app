package com.liquotrack.stocksip.shared.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

/**
 * Wrapper component that combines Drawer, TopBar and Scaffold
 * Use this for main screens that need navigation drawer
 *
 * @param title Screen title for TopBar
 * @param currentRoute Current route for highlighting drawer item
 * @param onNavigate Navigation callback
 * @param showBackButton Whether to show back button instead of menu icon
 * @param onBackClick Callback for back button (if showBackButton is true)
 * @param topBarActions Optional actions for TopBar (search, filter, etc)
 * @param enableDrawerGestures Enable swipe gesture to open drawer
 * @param backgroundColor Background color for the screen
 * @param content Screen content
 */
@Composable
fun DrawerScaffold(
    title: String,
    currentRoute: String,
    onNavigate: (String) -> Unit,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    topBarActions: @Composable () -> Unit = {},
    enableDrawerGestures: Boolean = true,
    backgroundColor: Color = Color(0xFFF4ECEC),
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                currentRoute = currentRoute,
                onNavigate = onNavigate,
                onClose = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        },
        gesturesEnabled = enableDrawerGestures
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = title,
                    showBackButton = showBackButton,
                    onNavigationClick = {
                        if (showBackButton) {
                            onBackClick()
                        } else {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    },
                    actions = topBarActions
                )
            },
            containerColor = backgroundColor,
            content = content
        )
    }
}