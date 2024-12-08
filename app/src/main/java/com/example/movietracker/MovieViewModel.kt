package com.example.movietracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    // LiveData pro seznam filmů
    private val _movies = MutableLiveData<List<MovieEntity>>()
    val movies: LiveData<List<MovieEntity>> get() = _movies

    private val movieApiService = ApiClient.retrofit.create(MovieApiService::class.java)

    fun fetchPopularMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = movieApiService.getPopularMovies(apiKey)
                _movies.value = response.results  // Nastavení hodnoty do LiveData
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

