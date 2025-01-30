package com.example.movieapp.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movies: List<MovieEntity>)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieEntity>


    @Query("SELECT * FROM movies WHERE title = :title LIMIT 1")
    suspend fun getMovieByTitle(title: String): MovieEntity?

    @Query("SELECT * FROM movies")
    fun getAllFavoriteMovies(): LiveData<List<MovieEntity>>
}
