package com.example.movieapp.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.db.MovieDatabase
import com.example.movieapp.db.MovieEntity
import com.example.movieapp.model.Movie
import com.example.movieapp.network.RetrofitInstance

//class MovieViewModel(application: Application) : AndroidViewModel(application) {
//    private val api = RetrofitInstance.api
//    private val movieDao = MovieDatabase.getDatabase(application).movieDao()
//
//    private val _movieList = MutableLiveData<List<MovieEntity>?>()
//    val movieList: LiveData<List<MovieEntity>?> get() = _movieList
//
//    val favoriteMovies: LiveData<List<MovieEntity>> = movieDao.getAllFavoriteMovies()
//
//    private val _error = MutableLiveData<String?>()
//    val error: LiveData<String?> get() = _error
//
//    fun fetchMovieList(apiKey: String) {
//        viewModelScope.launch {
//            try {
//                val response = api.getMovieList(apiKey)
//                val movieEntities = response.results.map { movie ->
//                    MovieEntity(
//                        title = movie.title,
//                        release_date = movie.release_date,
//                        overview = movie.overview,
//                        poster_path = movie.poster_path.toString(),
//                        popularity = movie.popularity,
//                        isFavorite = false
//                    )
//                }
//                movieDao.insertMovie(movieEntities)
//                _movieList.postValue(movieEntities)
//
//            } catch (e: Exception) {
//                val cachedMovies = movieDao.getAllMovies()
//                _movieList.postValue(cachedMovies)
//                println("Error: ${e.message}, Loading from local database")
//            }
//        }
//    }
//    fun toggleFavorite(movie: MovieEntity) {
//        viewModelScope.launch {
//            val existingMovie = movieDao.getMovieByTitle(movie.title)
//
//            if (existingMovie != null) {
//                movieDao.deleteMovie(existingMovie)
//            } else {
//                movieDao.insertMovie(listOf(movie.copy(isFavorite = true)))
//            }
//        }
//    }
//}

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val api = RetrofitInstance.api
    private val movieDao = MovieDatabase.getDatabase(application).movieDao()

    private val _movieList = MutableLiveData<List<MovieEntity>?>()
    val movieList: LiveData<List<MovieEntity>?> get() = _movieList

    val favoriteMovies: LiveData<List<MovieEntity>> = movieDao.getAllFavoriteMovies()

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchMovieList(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = api.getMovieList(apiKey)
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
                movieDao.insertMovie(movieEntities)
                _movieList.postValue(movieEntities)

            } catch (e: Exception) {
                val cachedMovies = movieDao.getAllMovies()
                _movieList.postValue(cachedMovies)
                println("Error: ${e.message}, Loading from local database")
            }
        }
    }

    fun toggleFavorite(movie: MovieEntity) {
        viewModelScope.launch {
            val existingMovie = movieDao.getMovieByTitle(movie.title)

            if (existingMovie != null) {
                movieDao.deleteMovie(existingMovie)
            } else {
                val movieWithFavoriteStatus = movie.copy(isFavorite = true)
                movieDao.insertMovie(listOf(movieWithFavoriteStatus))
            }
        }
    }
}
