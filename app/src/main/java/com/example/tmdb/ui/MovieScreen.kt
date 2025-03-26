package com.example.tmdb.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tmdb.viewModel.MovieViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Composable que muestra una lista de card de las peliculas de toda la Api
 * @param movieViewModel Llamada al ViewModel
 * @param navController Manejo de la navegacion
 */
@Composable
fun MovieScreen(
    movieViewModel: MovieViewModel,
    navController: NavController,
) {
    val listState = rememberLazyGridState()

    /**
     * Muestra las peliculas de cada pagina conforme se deslice hacia abajo, lo que mostrara la siguiente pagina
     * de peliculas al llegar a la penultima
     */
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
            MovieCard(item, navController, movieViewModel)
        }

        //Muestra un CircularPrgressIndicator hasta que muestre las siguentes peliculas
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
