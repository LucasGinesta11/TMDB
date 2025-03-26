package com.example.tmdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.tmdb.navigation.AppNavigation
import com.example.tmdb.viewModel.MovieViewModel

/**
 * MainActivity que llama a AppNavigation para iniciar la aplicacion de acuerdo a lo que se ha indicado
 * como primera pantalla por ejemplo
 */
class MainActivity : ComponentActivity() {
    private val movieViewModel: MovieViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            AppNavigation(navController = rememberNavController(), movieViewModel = movieViewModel)

        }
    }
}
