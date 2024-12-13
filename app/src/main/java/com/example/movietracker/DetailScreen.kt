package com.example.movietracker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.movietracker.MovieDatabase
import com.example.movietracker.MovieFavouriteEntity
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
        }


        // Přidání posluchače pro tlačítko "Add to Favourites"
        binding.addToFavouritesButton.setOnClickListener {
            movie?.let {
                val favouriteMovie = MovieFavouriteEntity(
                    id = it.id,
                    title = it.title,
                    poster_path = "https://image.tmdb.org/t/p/w500${it.poster_path}",
                    vote_average = it.vote_average,
                    release_date = it.release_date,
                    overview = it.overview
                )

                CoroutineScope(Dispatchers.IO).launch {
                    movieDao.addFavourite(favouriteMovie)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@DetailScreen, "Added to Favourites", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}











