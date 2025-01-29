package com.example.movieapp.nav


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Movies : BottomNavItem("movieScreen", "Movies", Icons.Filled.Home)
    object Favorites : BottomNavItem("favoriteScreen", "Favorites", Icons.Filled.Favorite)
}
