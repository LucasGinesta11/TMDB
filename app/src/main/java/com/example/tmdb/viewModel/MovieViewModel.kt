package com.example.tmdb.viewModel

import android.app.Application
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto.room.database.AppDatabase
import com.example.tmdb.api.MovieAPI
import com.example.tmdb.model.Item
import com.example.tmdb.room.MovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//Acceso al Application para inicializar Room, y usa application para obtener el contexto
class MovieViewModel(application: Application) : AndroidViewModel(application) {

    var selectedMovie = mutableStateOf<Item?>(null)
    var currentPage = 1
    val items = mutableListOf<Item>()

    //Comprueba si esta cargando contenido para evitar llamadas multiples
    var isLoading = mutableStateOf(false)

    //Dao para interactuar con la bd
    private val movieDao = AppDatabase.getDatabase(application).movieDao()

    //Flow (manejo de flujos eficiente) con lista de favoritos de Room
    val favoriteMovies: Flow<List<MovieEntity>> = movieDao.getAllFavorites()

    var searchQuery = mutableStateOf("")

    //Carga de peliculas al iniciar
    init {
        loadMoreItems()
    }

    fun setSelectedMovie(movie: Item) {
        selectedMovie.value = movie
    }

    //Carga peliculas y aumenta el contador de pagina para poder mostrar mas

    fun loadMoreItems() {
        if (isLoading.value) return
        if (!isOnline()) return

        isLoading.value = true
        viewModelScope.launch {
            try {
                val movies = MovieAPI.fetchMovies(currentPage)
                // Filtra por título si hay texto en el campo de búsqueda
                val filteredMovies = if (searchQuery.value.isNotBlank()) {
                    movies.filter { it.title.contains(searchQuery.value, ignoreCase = true) }
                } else {
                    movies
                }
                if (filteredMovies.isNotEmpty()) {
                    items.addAll(filteredMovies)
                    currentPage++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }

    fun addFavorite(movie: Item) {
        val favoriteMovie = movie.toFavoriteMovie()
        viewModelScope.launch {
            movieDao.insertFavorite(favoriteMovie)
        }
    }

    fun removeFavorite(movie: Item) {
        val favoriteMovie = movie.toFavoriteMovie()
        viewModelScope.launch {
            movieDao.deleteFavorite(favoriteMovie)
        }
    }

    fun toggleFavorite(movie: Item) {
        viewModelScope.launch {
            //Lista actual de favoritos
            val favorites = favoriteMovies.first()

            //Verifica si esta en favoritos para poder agregarla o eliminarla
            val isFavorite = favorites.any { it.id == movie.id }
            if (isFavorite) {
                removeFavorite(movie)
            } else {
                addFavorite(movie)
            }
        }
    }

    fun Item.toFavoriteMovie(): MovieEntity {
        return MovieEntity(
            id = id,
            title = title,
            posterPath = poster_path ?: "",
            releaseDate = release_date,
            backdrop_path = backdrop_path ?: "",
            overview = overview,
            adult = adult,
            vote_average = vote_average,
            original_language = original_language,
            popularity = popularity
        )
    }

    fun isOnline(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
        currentPage = 1
        items.clear()
        loadMoreItems()
    }
}