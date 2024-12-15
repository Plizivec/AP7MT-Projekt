package com.example.movietracker

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movietracker.databinding.ItemMovieBinding
import com.bumptech.glide.Glide

class MovieAdapter : ListAdapter<MovieEntity, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieEntity) {
            Glide.with(binding.posterImageView.context)
                .load("https://image.tmdb.org/t/p/w500/${movie.poster_path}")
                .into(binding.posterImageView)
            binding.titleTextView.text = movie.title
            binding.voteAverageTextView.text = "Rating: ${movie.vote_average}"

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailScreen::class.java)
                intent.putExtra("movieDetails", movie)  // Předání MovieEntity
                context.startActivity(intent)
            }
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<MovieEntity>() {
        override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem == newItem
        }
    }
}







