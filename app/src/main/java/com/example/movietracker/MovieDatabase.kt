package com.example.movietracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [MovieFavouriteEntity::class, MovieHistoryEntity::class], version = 2)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        // Definujeme migraci z verze 1 na verzi 2
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Vytvoření tabulky recent_history
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `recent_history` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `title` TEXT, 
                        `poster_path` TEXT, 
                        `vote_average` REAL, 
                        `overview` TEXT, 
                        `release_date` TEXT
                    )
                """)
            }
        }

        // Vytvoření instance databáze a přidání migrace
        fun getDatabase(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie-database"
                )
                    .addMigrations(MIGRATION_1_2) // Přidání migrace
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}





