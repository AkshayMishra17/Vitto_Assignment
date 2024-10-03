package com.example.movieapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/searchMovie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ):MovieResponse
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: String, @Query("api_key") apiKey: String): Movie

    data class MovieResponse(
        val results: List<Movie>
    )
}

object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
