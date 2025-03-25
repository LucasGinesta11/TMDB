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

@Composable
fun MovieCard(favoriteMovie: MovieEntity, navController: NavController, movieViewModel: MovieViewModel) {
    val imageUrl = "https://image.tmdb.org/t/p/w500${favoriteMovie.posterPath}"
    val favorites by movieViewModel.favoriteMovies.collectAsState(initial = emptyList())
    val isFavorite = favorites.any { it.id == favoriteMovie.id }

    Card(
        modifier = Modifier
            .height(350.dp)
            .clickable {
                movieViewModel.setSelectedMovie(Item(
                    id = favoriteMovie.id,
                    title = favoriteMovie.title,
                    poster_path = favoriteMovie.posterPath,
                    release_date = favoriteMovie.releaseDate,
                    backdrop_path = "", // Si no necesitas este campo, puedes omitirlo
                    overview = "", // Si no necesitas este campo, puedes omitirlo
                    adult = false, // O el valor que necesites
                    vote_average = 0.0, // O el valor que necesites
                    original_language = "", // O el valor que necesites
                    popularity = 0.0 // O el valor que necesites
                ))
                navController.navigate("MovieDetails/${favoriteMovie.id}")
            },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Surface(shape = RoundedCornerShape(8.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .error(R.drawable.imagen)
                        .build(),
                    contentDescription = "Portada de la película"
                )
                IconButton(onClick = {
                    movieViewModel.toggleFavorite(Item(
                        id = favoriteMovie.id,
                        title = favoriteMovie.title,
                        poster_path = favoriteMovie.posterPath,
                        release_date = favoriteMovie.releaseDate,
                        backdrop_path = "", // Si no necesitas este campo, puedes omitirlo
                        overview = "", // Si no necesitas este campo, puedes omitirlo
                        adult = false, // O el valor que necesites
                        vote_average = 0.0, // O el valor que necesites
                        original_language = "", // O el valor que necesites
                        popularity = 0.0 // O el valor que necesites
                    ))
                }) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Favorito",
                        tint = if (isFavorite) Color(0xFFFF0000) else Color(0xFFACACAC)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Titulo: ${favoriteMovie.title}", color = Color.Blue, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = favoriteMovie.releaseDate, color = Color.Blue)
        }
    }
}
