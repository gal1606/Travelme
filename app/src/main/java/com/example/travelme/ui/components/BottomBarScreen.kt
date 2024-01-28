package com.example.travelme.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : BottomBarScreen(
        route = "HOME",
        title = "HOME",
        icon = Icons.Default.Home
    )

    data object Profile : BottomBarScreen(
        route = "PROFILE",
        title = "PROFILE",
        icon = Icons.Default.Person
    )

    data object Search : BottomBarScreen(
        route = "SEARCH",
        title = "SEARCH",
        icon = Icons.Default.Search
    )

    data object Users : BottomBarScreen(
        route = "HOME",
        title = "USERS",
        icon = Icons.Default.AccountBox
    )

    data object Pending : BottomBarScreen(
        route = "PENDING",
        title = "PENDING",
        icon = Icons.Default.Refresh
    )

    data object Logout : BottomBarScreen(
        route = "LOGOUT",
        title = "LOGOUT",
        icon = Icons.Default.Lock
    )
}
