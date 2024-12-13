package com.example.movietracker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    // LiveData pro seznam filmů
    private val _movies = MutableLiveData<List<MovieEntity>>()
    val movies: LiveData<List<MovieEntity>> get() = _movies

    // LiveData pro oblíbené filmy
    private val _favouriteMovies = MutableLiveData<List<MovieFavouriteEntity>>()
    val favouriteMovies: LiveData<List<MovieFavouriteEntity>> get() = _favouriteMovies

    private val movieApiService = ApiClient.retrofit.create(MovieApiService::class.java)
    private val movieDao = MovieDatabase.getDatabase(App.instance).movieDao()  // Zajistí přístup k databázi

    // Načítání populárních filmů z API
    fun fetchPopularMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = movieApiService.getPopularMovies(apiKey)
                val movies = response.results.map {
                    MovieEntity(
                        id = it.id,
                        title = it.title,
                        poster_path = "https://image.tmdb.org/t/p/w500${it.poster_path}",
                        vote_average = it.vote_average,
                        overview = it.overview,
                        release_date = it.release_date
                    )
                }
                _movies.value = movies
                Log.d("API_RESPONSE", "Movies: $movies")
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error fetching movies: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // Přidání filmu mezi oblíbené
    fun addFavourite(movie: MovieFavouriteEntity) {
        viewModelScope.launch {
            try {
                movieDao.addFavourite(movie)  // Uložení oblíbeného filmu
                loadFavouriteMovies()  // Obnovíme seznam oblíbených filmů
            } catch (e: Exception) {
                Log.e("DB_ERROR", "Error adding favourite: ${e.message}")
            }
        }
    }

    // Načítání oblíbených filmů
    fun loadFavouriteMovies() {
        viewModelScope.launch {
            try {
                val favouriteMovies = movieDao.getFavourites()  // Načtení oblíbených filmů z databáze
                _favouriteMovies.value = favouriteMovies
            } catch (e: Exception) {
                Log.e("DB_ERROR", "Error loading favourites: ${e.message}")
            }
        }
    }

    // Odstranění oblíbeného filmu
    fun removeFavourite(movie: MovieFavouriteEntity) {
        viewModelScope.launch {
            try {
                movieDao.deleteFavourite(movie)  // Odstranění filmu z oblíbených
                loadFavouriteMovies()  // Obnovíme seznam oblíbených filmů
            } catch (e: Exception) {
                Log.e("DB_ERROR", "Error removing favourite: ${e.message}")
            }
        }
    }
}



