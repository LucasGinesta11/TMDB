package com.example.tmdb.ui

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tmdb.model.Item
import com.example.tmdb.viewModel.MovieViewModel

/**
 * Composable que muestras los card de las peliculas agregadas en favoritos
 * @param movieViewModel Llamada al ViewModel
 * @param navController Manejo de la navegacion
 */
@Composable
fun MovieFavourite(movieViewModel: MovieViewModel, navController: NavController) {
    //Comprueba el conjunto de peliculas agregadas a favoritos
    val favorites by movieViewModel.favoriteMovies.collectAsState(initial = emptyList())
    //Mapea las peliculas agregadas en favoritos
    val favoriteItems: List<Item> = favorites.map { it.toItem() }

    /**
     * LazyVerticalGrid que muestra los card de las peliculas
     */
    LazyVerticalGrid(
        columns = GridCells.Adaptive(200.dp)
    ) {
        items(favoriteItems) { favoriteMovie ->
            MovieCard(
                item = favoriteMovie,
                navController = navController,
                movieViewModel = movieViewModel
            )
        }
    }
}