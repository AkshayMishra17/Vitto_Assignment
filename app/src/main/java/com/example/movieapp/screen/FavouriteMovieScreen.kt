package com.example.movieapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.model.Movie

@Composable
fun FavoriteMoviesScreen(navController: NavHostController, viewModel: MovieViewModel) {
    val favoriteMovies = viewModel.favoriteMovies.observeAsState(emptyList()).value

    Column(modifier = Modifier.fillMaxSize()) {
        if (favoriteMovies.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No favorite movies added yet.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(favoriteMovies) { movie ->
                    MovieCard(movie = movie, viewModel = viewModel, navController = navController)
                }
            }
        }
    }
}