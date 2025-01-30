package com.example.movieapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.model.Movie

@Composable
fun FavoriteMoviesScreen(navController: NavHostController, viewModel: MovieViewModel) {
    val favoriteMovies = viewModel.favoriteMovies.observeAsState(emptyList()).value
    val allMovies = viewModel.movieList.observeAsState().value?.map { entity ->
        Movie(
            title = entity.title,
            release_date = entity.release_date,
            overview = entity.overview,
            poster_path = entity.poster_path,
            popularity = entity.popularity
        )
    } ?: emptyList()

    val filteredFavorites = allMovies.map { movie ->
        movie.poster_path?.let {
            MovieEntity(
                title = movie.title,
                release_date = movie.release_date,
                overview = movie.overview,
                poster_path = it,
                popularity = movie.popularity,
                isFavorite = true // or false, depending on your needs
            )
        }
    }.filter { movie ->
        favoriteMovies.any { it.poster_path == movie?.poster_path }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (filteredFavorites.isEmpty()) {
            Text(
                "No favorite movies added yet.",
                modifier = Modifier.fillMaxSize(),
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(filteredFavorites) { _, movie ->
                    if (movie != null) {
                        MovieCard(movie = movie, viewModel = viewModel, navController = navController)
                    }
                }
            }
        }
    }
}
