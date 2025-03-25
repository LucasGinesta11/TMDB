package com.example.tmdb.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tmdb.model.Item
import com.example.tmdb.viewModel.MovieViewModel

@Composable
fun MovieDetails(item: Item, movieViewModel: MovieViewModel) {
    val imageUrl = "https://image.tmdb.org/t/p/w500${item.backdrop_path}"
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Imagen trasera de la película",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }
        item {
            Text(text = item.title, style = MaterialTheme.typography.bodyLarge, fontSize = 30.sp)
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