package com.example.tmdb.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tmdb.model.Item
import com.example.tmdb.room.MovieEntity
import com.example.tmdb.ui.MovieCard
import com.example.tmdb.viewModel.MovieViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun MovieScreen(
    movieViewModel: MovieViewModel,
    navController: NavController,
) {
    val listState = rememberLazyGridState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .map { it.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastIndex ->
                if (lastIndex != null && lastIndex >= movieViewModel.items.size - 1 && !movieViewModel.isLoading.value) {
                    movieViewModel.loadMoreItems()
                    Log.d("Carga", "Carga pagina ${movieViewModel.currentPage}")
                }
            }
    }

    LazyVerticalGrid(
        state = listState,
        columns = GridCells.Adaptive(200.dp)
    ) {
        items(movieViewModel.items) { item ->
            // Aquí debes convertir el Item a MovieEntity
            val movieEntity = MovieEntity(
                id = item.id,
                title = item.title,
                posterPath = item.poster_path,
                releaseDate = item.release_date,
                adult = item.adult,
                backdrop_path = item.backdrop_path,
                original_language = item.original_language,
                overview = item.overview,
                popularity = item.popularity,
                vote_average = item.vote_average
            )
            MovieCard(movieEntity, navController, movieViewModel)
        }

        if (movieViewModel.isLoading.value) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}
