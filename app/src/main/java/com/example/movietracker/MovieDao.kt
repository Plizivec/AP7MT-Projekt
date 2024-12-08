package com.example.movietracker

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.movietracker.MovieEntity

@Dao
interface MovieDao {

    @Insert
    suspend fun insertMovie(movie: MovieEntity): Long // Vrací ID nového záznamu

    @Delete
    suspend fun deleteMovie(movie: MovieEntity): Int // Vrací počet odstraněných řádků

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieEntity> // Vrací seznam entit MovieEntity
}


