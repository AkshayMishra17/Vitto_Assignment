package com.example.movieapp.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.db.MovieDatabase
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.network.RetrofitInstance

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val api = RetrofitInstance.api
    private val movieDao = MovieDatabase.getDatabase(application).movieDao()

    private val _movieList = MutableLiveData<List<MovieEntity>>()
    val movieList: LiveData<List<MovieEntity>> get() = _movieList

    val favoriteMovies: LiveData<List<MovieEntity>> = movieDao.getAllFavoriteMovies()

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private var currentPage = 1
    private var isLoading = false
    private val loadedMovies = mutableListOf<MovieEntity>()

    fun fetchMovieList(apiKey: String, reset: Boolean = false) {
        if (isLoading) return

        isLoading = true
        viewModelScope.launch {
            try {
                val response = api.getMovieList(apiKey, page = currentPage)
                val movieEntities = response.results.map { movie ->
                    MovieEntity(
                        title = movie.title,
                        release_date = movie.release_date,
                        overview = movie.overview,
                        poster_path = movie.poster_path.toString(),
                        popularity = movie.popularity,
                        isFavorite = false
                    )
                }

                loadedMovies.addAll(movieEntities)
                movieDao.insertMoviesPreserveFavorites(movieEntities)

                val updatedMovies = movieDao.getAllMovies()
                _movieList.postValue(updatedMovies)

                currentPage++

            } catch (e: Exception) {
                val cachedMovies = movieDao.getAllMovies()
                _movieList.postValue(cachedMovies)
                _error.postValue("Error: ${e.message}, Loading from local database")
            } finally {
                isLoading = false
            }
        }
    }

    fun toggleFavorite(movie: MovieEntity) {
        viewModelScope.launch {
            try {
                val existingMovie = movieDao.getMovieByTitle(movie.title)
                if (existingMovie != null) {
                    val updatedMovie = existingMovie.copy(isFavorite = !existingMovie.isFavorite)
                    movieDao.updateMovie(updatedMovie)
                } else {
                    // If movie doesn't exist, add it as a favorite
                    val newMovie = movie.copy(isFavorite = true)
                    movieDao.insertMovie(listOf(newMovie))
                }
            } catch (e: Exception) {
                _error.postValue("Error updating favorite: ${e.message}")
            }
        }
    }
}
