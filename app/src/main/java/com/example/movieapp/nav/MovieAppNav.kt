package com.example.movieapp.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.screen.FavoriteMoviesScreen
import com.example.movieapp.screen.MovieDetailScreen
import com.example.movieapp.screen.MovieScreen
import com.example.movieapp.screen.MovieViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun MovieAppNav() {
    val navController = rememberNavController()
    val viewModel:MovieViewModel = viewModel()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "movieScreen",
            modifier = androidx.compose.ui.Modifier.padding(paddingValues)
        ) {
            composable("movieScreen") {
                MovieScreen(navController = navController)
            }
            composable(
                "movieDetail/{title}/{release_date}/{overview}/{posterPath}/{popularity}"
            ) { backStackEntry ->
                val title = backStackEntry.arguments?.getString("title")?.let {
                    URLDecoder.decode(it, StandardCharsets.UTF_8.name())
                }
                val releaseDate = backStackEntry.arguments?.getString("release_date")?.let {
                    URLDecoder.decode(it, StandardCharsets.UTF_8.name())
                }
                val overview = backStackEntry.arguments?.getString("overview")?.let {
                    URLDecoder.decode(it, StandardCharsets.UTF_8.name())
                }
                val posterPath = backStackEntry.arguments?.getString("posterPath")?.let {
                    URLDecoder.decode(it, StandardCharsets.UTF_8.name())
                }
                val popularity = backStackEntry.arguments?.getString("popularity")?.toFloatOrNull() ?: 0.0f
                MovieDetailScreen(title, releaseDate, overview, posterPath, popularity)
            }
            composable("favoriteScreen") {
                FavoriteMoviesScreen(navController = navController,viewModel)
            }
        }
    }
}
