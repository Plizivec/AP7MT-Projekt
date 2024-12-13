package com.example.movietracker

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {

    @Insert
    suspend fun addFavourite(movie: MovieFavouriteEntity)

    @Query("SELECT * FROM favourites")
    suspend fun getFavourites(): List<MovieFavouriteEntity>

    @Delete
    suspend fun deleteFavourite(movie: MovieFavouriteEntity)
}



