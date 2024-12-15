package com.example.movietracker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.movietracker.databinding.ActivityDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailScreen : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Získání informací o filmu z Intentu
        val movie: MovieEntity? = intent.getParcelableExtra("movieDetails")

        // Získání instance databáze
        val database = MovieDatabase.getDatabase(this)
        val movieDao = database.movieDao()

        // Nastavení dat filmu na obrazovku
        movie?.let {
            binding.titleTextView.text = it.title
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500/${it.poster_path}")
                .into(binding.posterImageView)
            binding.voteAverageTextView.text = "Rating: ${it.vote_average}"
            binding.releaseDateTextView.text = "Release Date: ${it.release_date}"
            binding.overviewTextView.text = it.overview

            // Přidání filmu do historie
            saveToHistory(movieDao, it)

            // Zjištění, zda je film v oblíbených
            CoroutineScope(Dispatchers.IO).launch {
                val isFavourite = movieDao.getFavourites().any { fav -> fav.id == it.id }

                withContext(Dispatchers.Main) {
                    if (isFavourite) {
                        setupRemoveFromFavourites(movieDao, it)
                    } else {
                        setupAddToFavourites(movieDao, it)
                    }
                }
            }
        }
    }

    private fun saveToHistory(movieDao: MovieDao, movie: MovieEntity) {
        val movieHistory = MovieHistoryEntity(
            id = movie.id,
            title = movie.title,
            poster_path = movie.poster_path,
            vote_average = movie.vote_average,
            release_date = movie.release_date,
            overview = movie.overview
        )

        CoroutineScope(Dispatchers.IO).launch {
            movieDao.addToHistory(movieHistory)
        }
    }

    private fun setupAddToFavourites(movieDao: MovieDao, movie: MovieEntity) {
        binding.addToFavouritesButton.text = "Add to Favourites"
        binding.addToFavouritesButton.setOnClickListener {
            val favouriteMovie = MovieFavouriteEntity(
                id = movie.id,
                title = movie.title,
                poster_path = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                vote_average = movie.vote_average,
                release_date = movie.release_date,
                overview = movie.overview
            )

            CoroutineScope(Dispatchers.IO).launch {
                movieDao.addFavourite(favouriteMovie)

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailScreen, "Added to Favourites", Toast.LENGTH_SHORT).show()
                    setupRemoveFromFavourites(movieDao, movie) // Přepnutí tlačítka na "Remove from Favourites"
                }
            }
        }
    }

    private fun setupRemoveFromFavourites(movieDao: MovieDao, movie: MovieEntity) {
        binding.addToFavouritesButton.text = "Remove from Favourites"
        binding.addToFavouritesButton.setOnClickListener {
            val favouriteMovie = MovieFavouriteEntity(
                id = movie.id,
                title = movie.title,
                poster_path = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                vote_average = movie.vote_average,
                release_date = movie.release_date,
                overview = movie.overview
            )

            CoroutineScope(Dispatchers.IO).launch {
                movieDao.deleteFavourite(favouriteMovie)

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailScreen, "Removed from Favourites", Toast.LENGTH_SHORT).show()
                    setupAddToFavourites(movieDao, movie) // Přepnutí tlačítka na "Add to Favourites"
                }
            }
        }
    }
}
















