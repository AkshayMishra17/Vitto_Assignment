package com.example.movieapp.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.Movie
import com.example.movieapp.network.RetrofitInstance

class MovieViewModel : ViewModel() {
    private val api = RetrofitInstance.api

    private val _movieList = MutableLiveData<List<Movie>?>()
    val movieList: LiveData<List<Movie>?> get() = _movieList

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _favorites = MutableLiveData<MutableSet<String>>(mutableSetOf()) // Use poster_path as unique identifier
    val favorites: LiveData<MutableSet<String>> get() = _favorites

    fun fetchMovieList(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = api.getMovieList(apiKey)
                _movieList.value = response.results
            } catch (e: HttpException) {
                _error.value = "HTTP error: ${e.message()}"
            } catch (e: IOException) {
                _error.value = "Network error: ${e.message}"
            } catch (e: Exception) {
                _error.value = "Unexpected error: ${e.message}"
            }
        }
    }

    fun toggleFavorite(posterPath: String) {
        val currentFavorites = _favorites.value ?: mutableSetOf()
        if (currentFavorites.contains(posterPath)) {
            currentFavorites.remove(posterPath)
        } else {
            currentFavorites.add(posterPath)
        }
        _favorites.value = currentFavorites
    }
}
