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

    private val _searchResults = MutableLiveData<List<MovieFavouriteEntity>>()
    val searchResults: LiveData<List<MovieFavouriteEntity>> get() = _searchResults

    private val _recentHistory = MutableLiveData<List<MovieHistoryEntity>>()
    val recentHistory: LiveData<List<MovieHistoryEntity>> get() = _recentHistory

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

    fun isFavourite(movieId: Int): LiveData<Boolean> {
        val isFavouriteLiveData = MutableLiveData<Boolean>()
        viewModelScope.launch {
            try {
                val favourites = movieDao.getFavourites()
                val isFavourite = favourites.any { it.id == movieId }
                isFavouriteLiveData.postValue(isFavourite)
            } catch (e: Exception) {
                Log.e("DB_ERROR", "Error checking favourite: ${e.message}")
            }
        }
        return isFavouriteLiveData
    }

    // Vyhledávání filmů podle názvu
    fun searchMovies(apiKey: String, query: String) {
        viewModelScope.launch {
            try {
                val response = movieApiService.searchMovies(query, apiKey)
                _movies.value = response.results
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error searching movies: ${e.message}")
            }
        }
    }

    // Třídění filmů podle hodnocení
    fun sortMoviesByRating(ascending: Boolean) {
        // Pokud je _movies.value null, tak použijeme prázdný seznam.
        val sortedMovies = _movies.value?.sortedBy {
            if (ascending) it.vote_average else -it.vote_average
        } ?: emptyList()  // Pokud je null, použijeme prázdný seznam

        // Nastavení nového seznamu filmů
        _movies.value = sortedMovies
    }

    // Přidání filmu do historie
    fun addToHistory(movie: MovieHistoryEntity) {
        viewModelScope.launch {
            try {
                // Pokud je v historii více než 20 filmů, odstraníme nejstarší
                val historyCount = movieDao.getHistoryCount()
                if (historyCount >= 20) {
                    movieDao.removeOldestHistory()
                }

                // Přidání filmu do historie
                movieDao.addToHistory(movie)
                loadRecentHistory() // Obnovení historie
            } catch (e: Exception) {
                Log.e("DB_ERROR", "Error adding to history: ${e.message}")
            }
        }
    }

    // Načítání historie
    fun loadRecentHistory() {
        viewModelScope.launch {
            try {
                val history = movieDao.getRecentHistory()
                _recentHistory.value = history
            } catch (e: Exception) {
                Log.e("DB_ERROR", "Error loading history: ${e.message}")
            }
        }
    }

}




