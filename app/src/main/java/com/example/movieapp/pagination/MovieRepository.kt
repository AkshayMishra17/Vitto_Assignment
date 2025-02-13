package com.example.movieapp.pagination

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import com.example.movieapp.network.ApiService
import com.example.movieapp.model.Movie

class MovieRepository(private val apiService: ApiService) {
    fun getPopularMovies(apiKey: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(apiService, apiKey) }
        ).flow
    }
}
