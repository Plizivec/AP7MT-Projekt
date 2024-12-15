package com.example.movietracker

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movietracker.databinding.ActivityMainBinding
import android.text.Editable
import android.text.TextWatcher

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

        // Přidáme pozorovatele na LiveData pro filmy
        movieViewModel.movies.observe(this, Observer { movies ->
            movieAdapter.submitList(movies)
        })

        // Načteme populární filmy z API při startu aplikace
        movieViewModel.fetchPopularMovies("eaf02c172acda2e10f42e919feaab5cc")

        // Nastavení listeneru pro vyhledávání
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                val query = editable.toString()
                if (query.isNotEmpty()) {
                    // Pokud je zadán text, hledáme filmy podle názvu
                    movieViewModel.searchMovies("eaf02c172acda2e10f42e919feaab5cc", query)
                } else {
                    // Pokud není zadán žádný text, načteme populární filmy
                    movieViewModel.fetchPopularMovies("eaf02c172acda2e10f42e919feaab5cc")
                }
            }
        })
    }

    // Inflační metoda pro menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Obsluha kliknutí na položky v menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favourite -> {
                // Přejít na obrazovku oblíbených filmů
                val intent = Intent(this, FavouriteScreen::class.java)
                startActivity(intent)
                true
            }
            R.id.action_history -> {
                // Přejít na obrazovku historie filmů
                val intent = Intent(this, RecentHistoryActivity::class.java)  // Odkaz na existující obrazovku historie
                startActivity(intent)
                true
            }
            R.id.action_sort_ascending -> {
                // Seřadit filmy vzestupně podle hodnocení
                movieViewModel.sortMoviesByRating(ascending = true)

                binding.recyclerView.smoothScrollToPosition(0)
                true
            }
            R.id.action_sort_descending -> {
                // Seřadit filmy sestupně podle hodnocení
                movieViewModel.sortMoviesByRating(ascending = false)

                binding.recyclerView.smoothScrollToPosition(0)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


