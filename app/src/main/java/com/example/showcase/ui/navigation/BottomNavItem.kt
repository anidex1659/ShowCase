package com.example.showcase.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector



data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: Any
)

val bottomNavItems = listOf(
    BottomNavItem(
        title = "Home",
        icon = Icons.Default.Home,
        route = HomeRoute
    ),
    BottomNavItem(
        title = "Library",
        icon = Icons.Default.LibraryBooks,
        route = LibraryRoute
    ),
    BottomNavItem(
        title = "Search",
        icon = Icons.Default.Search,
        route = SearchRoute
    ),
    BottomNavItem(
        title = "Settings",
        icon = Icons.Default.Settings,
        route = SettingsRoute
    )
)