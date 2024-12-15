package com.example.movietracker

import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ): MovieApiResponse
}

data class MovieResponse(
    val results: List<MovieEntity>
)

data class MovieApiResponse(
    val results: List<MovieEntity>
)