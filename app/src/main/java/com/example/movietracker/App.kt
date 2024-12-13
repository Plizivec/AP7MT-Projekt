package com.example.movietracker

import android.app.Application

class App : Application() {

    // Zajišťuje, že instance aplikace bude dostupná pro celý projekt
    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this  // Uložíme instanci aplikace
    }
}
