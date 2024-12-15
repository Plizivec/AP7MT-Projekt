package com.example.movietracker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_history")
data class MovieHistoryEntity(
    @PrimaryKey val id: Int, // ID filmu, aby nedocházelo k duplicitám
    val title: String,
    val poster_path: String?,
    val vote_average: Float,
    val release_date: String,
    val overview: String,
    val timestamp: Long = System.currentTimeMillis() // Čas přidání (pro třídění a odstranění)
)
