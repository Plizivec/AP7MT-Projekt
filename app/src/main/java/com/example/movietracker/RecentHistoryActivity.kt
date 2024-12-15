package com.example.movietracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecentHistoryActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieViewModel
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent_history)

        // Inicializace ViewModelu
        viewModel = ViewModelProvider(this)[MovieViewModel::class.java]

        // Inicializace RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = MovieAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Pozorování změn v historii
        viewModel.recentHistory.observe(this) { history ->
            adapter.submitList(history.map { movie ->
                MovieEntity(
                    id = movie.id,
                    title = movie.title,
                    poster_path = movie.poster_path,
                    vote_average = movie.vote_average,
                    overview = movie.overview,
                    release_date = movie.release_date
                )
            })
        }

        // Načtení historie
        viewModel.loadRecentHistory()
    }
}
