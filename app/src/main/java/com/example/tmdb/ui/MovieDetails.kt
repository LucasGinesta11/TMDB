package com.example.tmdb.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tmdb.model.Item
import com.example.tmdb.viewModel.MovieViewModel

/**
 * Composable que muestra los detalles de la pelicula seleccionada
 *
 * @param item Parametro que obtiene los atributos de Item
 * @param movieViewModel Llamada al ViewModel
 */
@Composable
fun MovieDetails(item: Item, movieViewModel: MovieViewModel) {
    // Portada de película
    val imageUrl = "https://image.tmdb.org/t/p/w500${item.backdrop_path}"
    // Comprueba el conjunto de películas agregadas a favoritos
    val favorites by movieViewModel.favoriteMovies.collectAsState(initial = emptyList())
    // Comprueba si la película está agregada a favoritos
    val isFavorite = favorites.any { it.id == item.id }

    /**
     * LazyColumn que ordena los atributos elegidos en columna
     */
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        // Muestra esta imagen si ha habido un error
                        .error(com.example.tmdb.R.drawable.imagen)
                        .build(),
                    contentDescription = "Imagen trasera de la película",
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = { movieViewModel.toggleFavorite(item) },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Añadir a favoritos",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 30.sp,
                color = Color.Blue
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "ID: ${item.id}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Descripción: ${item.overview}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "+18: ${if (item.adult) "Sí" else "No"}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Idioma: ${item.original_language}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Popularidad: ${item.popularity}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Fecha de estreno: ${item.release_date}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Puntuación: ${item.vote_average}")
        }
    }
}
