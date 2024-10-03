package com.example.movieapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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


@Composable
fun MovieScreen(viewModel: MovieViewModel = viewModel()) {
    // Example API key and movie ID, replace with actual values
    val apiKey = "08bedb166ad78bd5457f4f73e7302b74"
    var movieId by remember { mutableStateOf("") }// Example movie ID, replace with actual movie ID

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(30.dp)) {
        OutlinedTextField(value = movieId, onValueChange = {movieId= it}, label = {Text(text ="Search a Movie")},
            modifier = Modifier.fillMaxWidth()

        )
        Button(onClick = {
            val movieId = movieId.toIntOrNull()
            if (movieId != null) {
                viewModel.fetchMovieDetails(movieId, apiKey)
            }
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
            Text(text = "Search")
        }
    }
  
    Spacer(modifier = Modifier.height(20.dp))


    // Fetch movie details if not already fetched
    val validMovieId = movieId.toIntOrNull()
    LaunchedEffect(validMovieId) {
        if (validMovieId != null) {
            viewModel.fetchMovieDetails(validMovieId, apiKey)
        }
    }

    val error by viewModel.error.observeAsState()
    val movieDetails by viewModel.movieDetails.observeAsState()

    if (error != null) {
        ErrorView(errorMessage = error)
    } else if (movieDetails != null) {
        MovieScreenView(movie = movieDetails)
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}


@Composable
fun MovieScreenView(movie: Movie?) {
    if (movie != null) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(35.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = movie.title,
                    modifier = Modifier.padding(bottom = 10.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            item {
                Text(
                    text = "Release Date: ${movie.release_date}",
                    modifier = Modifier.padding(bottom = 10.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            item {
                Text(
                    text = movie.overview,
                    modifier = Modifier.padding(bottom = 10.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            // Optional: Display poster image if available

        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
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

