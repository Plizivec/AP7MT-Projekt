package com.example.movietracker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Použití SplashScreen API (automaticky pro Android 12+)
        installSplashScreen()

        // Spuštění hlavní aktivity po zobrazení splash screenu
        startActivity(Intent(this, MainActivity::class.java))
        finish() // Ukončíme SplashActivity, aby uživatel nemohl přejít zpět
    }
}