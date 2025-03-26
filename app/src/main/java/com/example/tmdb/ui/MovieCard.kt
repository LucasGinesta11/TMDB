package com.example.tmdb.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tmdb.R
import com.example.tmdb.model.Item
import com.example.tmdb.room.MovieEntity
import com.example.tmdb.viewModel.MovieViewModel

/**
 * Composable que maneja la lista de cards para cada pelicula
 *
 * @param item Parametro que obtiene los atributos de Item
 * @param navController Manejo de la navegacion
 * @param movieViewModel Llamada al ViewModel
 */
@Composable
fun MovieCard(item: Item, navController: NavController, movieViewModel: MovieViewModel) {
    //Portada de pelicula
    val imageUrl = "https://image.tmdb.org/t/p/w500${item.poster_path}"
    //Comprueba el conjunto de peliculas agregadas a favoritos
    val favorites by movieViewModel.favoriteMovies.collectAsState(initial = emptyList())
    //Comprueba si la pelicula esta agregada a favoritos
    val isFavorite = favorites.any { it.id == item.id }

    /**
     * Card con el conjunto de propiedades y atributos de item
     */
    Card(
        modifier = Modifier
            .height(350.dp)
            .clickable {
                movieViewModel.setSelectedMovie(item)
                navController.navigate("MovieDetails/${item.id}")
            },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Surface(shape = RoundedCornerShape(8.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .error(R.drawable.imagen)
                        .build(),
                    contentDescription = "Portada de la película"
                )
                IconButton(onClick = {
                    movieViewModel.toggleFavorite(item)
                }) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Favorito",
                        tint = if (isFavorite) Color(0xFFFF0000) else Color(0xFFACACAC)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Título: ${item.title}",
                color = Color.Blue,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = item.release_date, color = Color.Blue)
        }
    }
}

fun MovieEntity.toItem(): Item {
    return Item(
        id = this.id,
        title = this.title,
        poster_path = this.poster_path,
        release_date = this.release_date,
        backdrop_path = this.backdrop_path,
        overview = this.overview,
        adult = this.adult,
        vote_average = this.vote_average,
        original_language = this.original_language,
        popularity = this.popularity
    )
}
