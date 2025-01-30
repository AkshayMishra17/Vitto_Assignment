package com.example.movieapp.network

import com.example.movieapp.model.Movie
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("movie/popular")
    suspend fun getMovieList(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int  // Add page parameter for pagination
    ): MovieResponse

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ): MovieResponse

    data class MovieResponse(
        val results: List<Movie>,
        val page: Int,
        val total_pages: Int
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