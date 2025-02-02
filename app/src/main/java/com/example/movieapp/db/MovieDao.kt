package com.example.movieapp.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {

    @Transaction
    suspend fun insertMoviesPreserveFavorites(movies: List<MovieEntity>) {
        movies.forEach { movie ->
            val existingMovie = getMovieByTitle(movie.title)
            if (existingMovie != null) {
                insertMovie(listOf(movie.copy(isFavorite = existingMovie.isFavorite)))
            } else {
                insertMovie(listOf(movie))
            }
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movies: List<MovieEntity>)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieEntity>

    @Update
    suspend fun updateMovie(movie: MovieEntity)

    @Query("SELECT * FROM movies WHERE title = :title LIMIT 1")
    suspend fun getMovieByTitle(title: String): MovieEntity?

    @Query("SELECT * FROM movies WHERE isFavorite = 1")
    fun getAllFavoriteMovies(): LiveData<List<MovieEntity>>
}
