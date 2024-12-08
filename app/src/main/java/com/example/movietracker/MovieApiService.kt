package com.example.movietracker

import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MovieResponse
}

data class MovieResponse(
    val results: List<MovieEntity> // Propojíme s naší entitou
)
