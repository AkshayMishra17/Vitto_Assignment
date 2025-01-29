package com.example.movieapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val id:Int=1,
    val title: String,
    val release_date: String,
    val overview: String,
    val poster_path: String,
    val popularity: Float,
    val isFavorite: Boolean = false
)
