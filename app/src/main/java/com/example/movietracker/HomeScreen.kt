package com.example.movietracker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(navController: NavController) {

    val viewModel: MovieViewModel = viewModel()
    // API klíč
    val apiKey = "eaf02c172acda2e10f42e919feaab5cc"

    // Zavolání funkce pro načtení dat
    viewModel.fetchPopularMovies(apiKey)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { navController.navigate("detail") }) {
            Text(text = "Go to Detail Screen")
        }
    }
}
