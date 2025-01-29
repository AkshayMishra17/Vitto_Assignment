package com.example.movieapp.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

//@Composable
//fun MovieDetailScreen(movieId: String?) {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(text = "Movie Details for: $movieId")
////        Text(text = "Movie Details for: ")
//    }
//}

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.movieapp.model.Movie
import com.google.gson.Gson
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@SuppressLint("DefaultLocale")
@Composable
fun MovieDetailScreen(title: String?, releaseDate: String?, overview: String?, posterPath: String?,popularity:Float) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        posterPath?.let {
            val imageUrl = "https://image.tmdb.org/t/p/original$it"
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = "Movie Poster",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(bottom = 16.dp)
            )
        }

        Text(
            text = title ?: "Unknown Title",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

        DetailItem(label = "📅 Release Date", value = releaseDate ?: "N/A")
        DetailItem(label = "📝 Overview", value = overview ?: "No description available.")
        DetailItem(label = "⭐ Popularity", value = "${String.format("%.1f", popularity)} / 10")

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
        )
    }
}