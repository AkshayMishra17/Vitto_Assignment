package com.example.movieapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.model.Movie
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

//08bedb166ad78bd5457f4f73e7302b74
@Composable
fun MovieScreen(navController: NavHostController, viewModel: MovieViewModel = viewModel()) {
    val apiKey = "08bedb166ad78bd5457f4f73e7302b74"

    LaunchedEffect(true) {
        viewModel.fetchMovieList(apiKey)
    }

    val error by viewModel.error.observeAsState()
    val movieList by viewModel.movieList.observeAsState()

    var searchQuery by remember { mutableStateOf("") }

    val filteredMovies = movieList?.map { entity ->
        Movie(
            title = entity.title,
            release_date = entity.release_date,
            overview = entity.overview,
            poster_path = entity.poster_path,
            popularity = entity.popularity
        )
    }?.filter { movie ->
        movie.title?.contains(searchQuery, ignoreCase = true) == true
    } ?: emptyList()

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search for a movie") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (error != null) {
            ErrorView(errorMessage = error)
        } else if (movieList != null) {
            MovieGridView(movies = filteredMovies, navController = navController, viewModel = viewModel)
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun MovieGridView(movies: List<Movie>, viewModel: MovieViewModel, navController: NavHostController) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        items(movies) { movie ->
            MovieCard(movie = movie, viewModel = viewModel, navController = navController)
        }
    }
}

@Composable
fun MovieCard(movie: Movie, viewModel: MovieViewModel, navController: NavHostController) {
    val favoriteMovies by viewModel.favoriteMovies.observeAsState(emptyList())
    val isFavorite = favoriteMovies.any { it.poster_path == movie.poster_path }

    Box(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate(
                "movieDetail/${URLEncoder.encode(movie.title, StandardCharsets.UTF_8.name())}/" +
                        "${URLEncoder.encode(movie.release_date, StandardCharsets.UTF_8.name())}/" +
                        "${URLEncoder.encode(movie.overview, StandardCharsets.UTF_8.name())}/" +
                        "${URLEncoder.encode(movie.poster_path ?: "", StandardCharsets.UTF_8.name())}/" +
                        "${movie.popularity}"
            )
            }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            movie.poster_path?.let {
                val imageUrl = "https://image.tmdb.org/t/p/original$it"
                Image(
                    painter = rememberImagePainter(imageUrl),
                    contentDescription = "Movie Poster",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(bottom = 10.dp)
                )
            }
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            IconButton(
                onClick = {
                    viewModel.toggleFavorite(
                        MovieEntity(
                            title = movie.title,
                            release_date = movie.release_date,
                            overview = movie.overview,
                            poster_path = movie.poster_path.toString(),
                            popularity = movie.popularity,
                            isFavorite = !isFavorite,
                        )
                    )
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (!isFavorite) Color.Red else Color.Gray
                )
            }
        }
    }
}

@Composable
fun ErrorView(errorMessage: String?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = errorMessage ?: "Unknown error", color = Color.Red)
    }
}

