package com.example.movieapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun FavoriteMoviesScreen(navController: NavHostController, viewModel: MovieViewModel) {
    val favorites = viewModel.favorites.observeAsState().value ?: mutableSetOf()
    val allMovies = viewModel.movieList.observeAsState().value ?: emptyList()

    LaunchedEffect(favorites) {
        println("Favorites updated: $favorites")
    }

    val favoriteMovies = allMovies.filter { movie ->
        favorites.contains(movie.poster_path)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (favoriteMovies.isEmpty()) {
            Text("No favorite movies added yet.", modifier = Modifier.fillMaxSize(), style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(favoriteMovies) { _, movie ->
                    MovieCard(movie = movie, viewModel = viewModel, navController = navController)
                }
            }
        }
    }
}
