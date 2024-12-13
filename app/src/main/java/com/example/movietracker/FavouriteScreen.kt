package com.example.movietracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movietracker.databinding.ActivityFavouriteBinding
import kotlinx.coroutines.launch

class FavouriteScreen : AppCompatActivity() {

    private lateinit var binding: ActivityFavouriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializujeme RecyclerView pro oblíbené filmy
        val movieAdapter = MovieAdapter()
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.adapter = movieAdapter

        // Načteme oblíbené filmy z databáze
        val movieDao = MovieDatabase.getDatabase(applicationContext).movieDao()
        lifecycleScope.launch {
            val favouriteMovies = movieDao.getFavourites()

            // Mapování na MovieEntity
            val movieList = favouriteMovies.map {
                MovieEntity(
                    id = it.id,  // Předpokládám, že máš stejné ID nebo podobná pole
                    title = it.title,
                    poster_path = it.poster_path,
                    vote_average = it.vote_average,
                    overview = it.overview,
                    release_date = it.release_date
                )
            }

            // Nyní předej seznam MovieEntity do adapteru
            movieAdapter.submitList(movieList)
        }
    }
}



