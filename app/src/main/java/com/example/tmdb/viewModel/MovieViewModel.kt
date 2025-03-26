@file:Suppress("DEPRECATION")

package com.example.tmdb.viewModel

import android.app.Application
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.api.MovieAPI
import com.example.tmdb.model.Item
import com.example.tmdb.room.AppDatabase
import com.example.tmdb.room.MovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel que maneja la mayor parte de la logica de la aplicacion
 * @param application Acceso a la aplicacion para obtener el contexto e inicializar Room
 * @property selectedMovie Pelicula que ha sido seleccionada
 * @property currentPage Pagina que se encuentra actual, y la que iniciara en el inicio de la aplicacion
 * @property items Lista modificable de Item
 * @property movieDao Dao para interactuar con la bd mediante consultas
 * @property favoriteMovies Lista Flow de las peliculas favoritas en la bd Room
 * @property searchQuery Consulta del buscador que empieza vacio
 */

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    var selectedMovie = mutableStateOf<Item?>(null)
    var currentPage = 1
    val items = mutableListOf<Item>()
    var isLoading = mutableStateOf(false)
    private val movieDao = AppDatabase.getDatabase(application).movieDao()
    val favoriteMovies: Flow<List<MovieEntity>> = movieDao.getAllFavorites()
    var searchQuery = mutableStateOf("")

    //Carga de peliculas al iniciar
    init {
        loadMoreItems()
    }

    fun setSelectedMovie(movie: Item) {
        selectedMovie.value = movie
    }

    /**
     * Carga peliculas y aumenta el contador de pagina para poder mostrar mas
     */
    fun loadMoreItems() {
        //Comprueba si ya se está cargando o si no hay conexión
        if (isLoading.value || !isOnline()) return

        viewModelScope.launch {
            isLoading.value = true

            //Intenta cargar los elementos de la API
            try {
                val nextItems = if (searchQuery.value.isNotEmpty()) {
                    MovieAPI.searchMovies(searchQuery.value, currentPage)
                } else {
                    MovieAPI.fetchMovies(currentPage)
                }

                if (nextItems.isNotEmpty()) {
                    if (currentPage == 1) {
                        //Reiniciar lista en búsquedas nuevas
                        items.clear()
                    }
                    items.addAll(nextItems)
                    currentPage++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }

    /**
     * Añade una pelicula a favoritos
     */
    fun addFavorite(movie: Item) {
        val favoriteMovie = movie.toFavoriteMovie()
        viewModelScope.launch {
            movieDao.insertFavorite(favoriteMovie)
        }
    }

    /**
     * Elimina una pelicula de favoritos
     */
    fun removeFavorite(movie: Item) {
        val favoriteMovie = movie.toFavoriteMovie()
        viewModelScope.launch {
            movieDao.deleteFavorite(favoriteMovie)
        }
    }

    /**
     * Metodo que comprueba si la pelicula esta en favoritos o no, lo que la agrega o la elimina
     */
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
            poster_path = poster_path.toString(),
            release_date = release_date,
            backdrop_path = backdrop_path.toString(),
            overview = overview.toString(),
            adult = adult,
            vote_average = vote_average,
            original_language = original_language.toString(),
            popularity = popularity
        )
    }

    /**
     * Comprueba si hay conexion, lo que evita que la aplicacion cierre si no lo hubiera
     * Si hay mostrara los datos, si no no mostrara nada
     */
    fun isOnline(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    //Manejo de la busqueda, como por que pagina tiene que iniciar o mostrar mas datos cada vez que se busca
    fun setSearchQuery(query: String) {
        searchQuery.value = query.trim()
        currentPage = 1
        items.clear()
        loadMoreItems()
    }


}