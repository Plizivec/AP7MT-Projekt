package com.example.movietracker

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movietracker.databinding.ActivityMainBinding
import androidx.navigation.compose.rememberNavController

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializujeme RecyclerView
        val movieAdapter = MovieAdapter()
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.adapter = movieAdapter

        // Přidáme pozorovatele na LiveData
        movieViewModel.movies.observe(this) { movies ->
            movieAdapter.submitList(movies)
        }

        // Načteme populární filmy z API
        movieViewModel.fetchPopularMovies("eaf02c172acda2e10f42e919feaab5cc")
    }

    // Inflační metoda pro menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Obsluha kliknutí na položky v menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favourite -> {
                val intent = Intent(this, FavouriteScreen::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
