package com.liquotrack.stocksip.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * StockSip Navigation Drawer Component
 *
 * @param username User's display name
 * @param currentRoute Currently selected route
 * @param onNavigate Callback for navigation with route destination
 * @param onClose Callback to close the drawer
 */
@Composable
fun NavDrawer(
    username: String = "Username",
    currentRoute: String = "home",
    onNavigate: (String) -> Unit = {},
    onClose: () -> Unit = {}
) {
    ModalDrawerSheet(
        drawerContainerColor = Color(0xFF4A1B2A),
        modifier = Modifier.width(280.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header Section
            DrawerHeader(username = username)

            Spacer(modifier = Modifier.height(24.dp))

            // Navigation Items
            DrawerNavigationItem(
                icon = Icons.Default.Home,
                title = "Home",
                route = "home",
                currentRoute = currentRoute,
                onClick = {
                    onNavigate("home")
                    onClose()
                }
            )

            DrawerNavigationItem(
                icon = Icons.Default.Warehouse,
                title = "Warehouse",
                route = "warehouse",
                currentRoute = currentRoute,
                onClick = {
                    onNavigate("warehouse")
                    onClose()
                }
            )

            DrawerNavigationItem(
                icon = Icons.AutoMirrored.Filled.MenuBook,
                title = "Care Guides",
                route = "care_guides",
                currentRoute = currentRoute,
                onClick = {
                    onNavigate("care_guides")
                    onClose()
                }
            )

            DrawerNavigationItem(
                icon = Icons.Default.ShoppingCart,
                title = "Orders",
                route = "orders",
                currentRoute = currentRoute,
                onClick = {
                    onNavigate("orders")
                    onClose()
                }
            )

            DrawerNavigationItem(
                icon = Icons.Default.Inventory,
                title = "Products",
                route = "products",
                currentRoute = currentRoute,
                onClick = {
                    onNavigate("products")
                    onClose()
                }
            )

            DrawerNavigationItem(
                icon = Icons.Default.LocalOffer,
                title = "Catalog",
                route = "catalog",
                currentRoute = currentRoute,
                onClick = {
                    onNavigate("catalog")
                    onClose()
                }
            )

            DrawerNavigationItem(
                icon = Icons.Default.CardMembership,
                title = "Plans",
                route = "plans",
                currentRoute = currentRoute,
                onClick = {
                    onNavigate("plans")
                    onClose()
                }
            )

            DrawerNavigationItem(
                icon = Icons.Default.AdminPanelSettings,
                title = "Admin Panel",
                route = "admin",
                currentRoute = currentRoute,
                onClick = {
                    onNavigate("admin")
                    onClose()
                }
            )

            DrawerNavigationItem(
                icon = Icons.Default.Person,
                title = "Profile",
                route = "profile",
                currentRoute = currentRoute,
                onClick = {
                    onNavigate("profile")
                    onClose()
                }
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun DrawerHeader(username: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // App Name
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        color = Color(0xFFE53E3E),
                        fontWeight = FontWeight.Bold
                    )
                    ) {
                        append("Stock")
                    }
                    withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                        append("Sip")
                    }
                },
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            // Arrow icon
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Arrow",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Username
        Text(
            text = username,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
}

@Composable
private fun DrawerNavigationItem(
    icon: ImageVector,
    title: String,
    route: String,
    currentRoute: String,
    onClick: () -> Unit
) {
    val isSelected = currentRoute == route
    val backgroundColor = if (isSelected) Color.White.copy(alpha = 0.15f) else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }

    Spacer(modifier = Modifier.height(4.dp))
}