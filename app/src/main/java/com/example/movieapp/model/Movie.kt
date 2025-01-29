package com.example.movieapp.model

data class Movie(
    val title:String,
    val release_date:String,
    val overview:String,
    val poster_path: String?,
    val isFavorite: Boolean = false,
    val popularity:Float
)


