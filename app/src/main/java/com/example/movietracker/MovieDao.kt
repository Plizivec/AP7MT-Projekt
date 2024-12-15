package com.example.movietracker

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy

@Dao
interface MovieDao {

    @Insert
    suspend fun addFavourite(movie: MovieFavouriteEntity)

    @Query("SELECT * FROM favourites")
    suspend fun getFavourites(): List<MovieFavouriteEntity>

    @Delete
    suspend fun deleteFavourite(movie: MovieFavouriteEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favourites WHERE id = :movieId)")
    suspend fun isMovieFavourite(movieId: Int): Boolean

    // Historie: Přidání filmu
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Přepíše film, pokud už existuje
    suspend fun addToHistory(movie: MovieHistoryEntity)

    // Historie: Získání všech filmů, seřazených podle času přidání (od nejnovějšího)
    @Query("SELECT * FROM recent_history ORDER BY timestamp DESC LIMIT 20")
    suspend fun getRecentHistory(): List<MovieHistoryEntity>

    // Historie: Odstranění nejstaršího filmu
    @Query("DELETE FROM recent_history WHERE timestamp = (SELECT MIN(timestamp) FROM recent_history)")
    suspend fun removeOldestHistory()

    // Historie: Získání počtu uložených filmů
    @Query("SELECT COUNT(*) FROM recent_history")
    suspend fun getHistoryCount(): Int

}




