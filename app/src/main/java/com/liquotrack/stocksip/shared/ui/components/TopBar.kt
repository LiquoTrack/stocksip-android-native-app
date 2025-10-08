package com.liquotrack.stocksip.shared.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * StockSip reusable TopBar component
 *
 * @param title The title to display in the TopBar
 * @param showBackButton Whether to show back arrow instead of menu icon
 * @param onNavigationClick Callback when navigation icon is clicked
 * @param actions Optional trailing actions (like search, filter, etc)
 * @param navigationIcon Optional custom navigation icon (defaults to Menu or ArrowBack)
 * @param backgroundColor Background color of the TopBar
 * @param contentColor Color for text and icons
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    showBackButton: Boolean = false,
    onNavigationClick: () -> Unit = {},
    actions: @Composable () -> Unit = {},
    navigationIcon: ImageVector? = null,
    backgroundColor: Color = Color(0xFFF4ECEC),
    contentColor: Color = Color(0xFF4A1B2A)
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = contentColor,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = navigationIcon ?: if (showBackButton) {
                        Icons.Default.ArrowBack
                    } else {
                        Icons.Default.Menu
                    },
                    contentDescription = if (showBackButton) "Back" else "Menu",
                    tint = contentColor
                )
            }
        },
        actions = { actions() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor
        )
    )
}

// Simplified TopBar with back button
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithMenu(
    title: String,
    onMenuClick: () -> Unit = {},
    actions: @Composable () -> Unit = {}
) {
    TopBar(
        title = title,
        showBackButton = false,
        onNavigationClick = onMenuClick,
        actions = actions
    )
}

// Simplified variant for screens with back navigation
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithBack(
    title: String,
    onBackClick: () -> Unit = {},
    actions: @Composable () -> Unit = {}
) {
    TopBar(
        title = title,
        showBackButton = true,
        onNavigationClick = onBackClick,
        actions = actions
    )
}