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

@Composable
fun MovieFavourite(movieViewModel: MovieViewModel, navController: NavController) {
    val favorites by movieViewModel.favoriteMovies.collectAsState(initial = emptyList())

    LazyVerticalGrid(
        columns = GridCells.Adaptive(200.dp)
    ) {
        items(favorites) { favoriteMovie ->
            MovieCard(
                favoriteMovie = favoriteMovie,
                navController = navController,
                movieViewModel = movieViewModel
            )
        }
    }
}