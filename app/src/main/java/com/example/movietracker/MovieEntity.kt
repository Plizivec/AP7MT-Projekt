package com.example.movietracker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
@Parcelize // Tato anotace automaticky vygeneruje potřebný kód pro Parcelable
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,                 // Interní ID pro databázi
    val title: String,               // Název filmu
    val poster_path: String?,         // URL plakátu filmu
    val vote_average: Float,
    val overview: String, // Hodnocení (např. 7.5)
    val release_date: String
) : Parcelable // Implementace Parcelable

