package com.example.movieapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MovieViewModel : ViewModel() {
    private val api = RetrofitInstance.api

    // LiveData for movie details
    private val _movieDetails = MutableLiveData<Movie?>()
    val movieDetails: LiveData<Movie?> get() = _movieDetails

    // LiveData for errors
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun searchMovie(query: String, apiKey: String) {
        viewModelScope.launch {
            try {
                // Call your API to search for the movie
                val response = api.searchMovie(query, apiKey)
                // Extract the first movie from the response results
                val movie = response.results.firstOrNull()
                _movieDetails.value = movie
            } catch (e: HttpException) {
                // Handle HTTP errors
                _error.value = "HTTP error: ${e.message()}"
            } catch (e: IOException) {
                // Handle network errors
                _error.value = "Network error: ${e.message}"
            } catch (e: Exception) {
                // Handle any other exceptions
                _error.value = "Unexpected error: ${e.message}"
            }
        }
    }


    fun fetchMovieDetails(movieId: Int, apiKey: String) {
        viewModelScope.launch {
            try {
                val details = api.getMovieDetails(movieId.toString(), apiKey)
                _movieDetails.postValue(details)  // Update LiveData with the fetched details
            } catch (e: HttpException) {
                // Handle HTTP errors
                _error.postValue("HTTP error: ${e.message()}")
            } catch (e: IOException) {
                // Handle network errors
                _error.postValue("Network error: ${e.message}")
            } catch (e: Exception) {
                // Handle any other exceptions
                _error.postValue("Unexpected error: ${e.message}")
            }
        }
    }
}

