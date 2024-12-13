package com.example.movietracker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class MovieFavouriteEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val poster_path: String,
    val vote_average: Float,
    val release_date: String,
    val overview: String
)

