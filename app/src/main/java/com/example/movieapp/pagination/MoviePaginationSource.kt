package com.example.movieapp.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.model.Movie
import com.example.movieapp.network.ApiService

class MoviePagingSource(
    private val apiService: ApiService,
    private val apiKey: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: 1
            val response = apiService.getMovieList(apiKey, currentPage)

            LoadResult.Page(
                data = response.results,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (response.results.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }
}
