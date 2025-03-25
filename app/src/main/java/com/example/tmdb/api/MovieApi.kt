package com.example.tmdb.api

import com.example.tmdb.model.Item
import com.example.tmdb.model.Movie
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz para la API de peliculas
 */
interface MovieAPIService {

    /**
     * EndPoint que devuelve las peliculas populares
     * @param apiKey Clave API para autenticar
     * @param page Pagina a cargar
     * @return Objeto Movie con las peliculas a partir de estos parametros
     */
    @GET("movie/popular")
    suspend fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Movie

    /**
     * EndPoint que busca peliculas a partir de una busqueda
     * @param apiKey Clave API para autenticar
     * @param query Consulta para encontrar la pelicula por su titulo
     * @param page Pagina a cargar
     * @return Objeto Movie con la busqueda de peliculas
     */
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Movie
}

/**
 * Singleton (unica instancia) que da acceso a la API
 * Uso de Retrofit para realizar solicitudes HTTP
 * @property BASE_URL La URL base de la API de películas.
 * @property API_KEY La clave API utilizada para autenticar las solicitudes.
 */
object MovieAPI {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val API_KEY = "13a30b1187404441e8327c0a2d58dc3f"

    /**
     * Lazy (se inicia cuando se llama por primera vez)
     * Build de Retrofit a partir de la URL
     */
    private val apiService: MovieAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieAPIService::class.java)
    }

    /**
     * Metodo que devuelve una lista de peliculas a partir de su pagina
     * @param page Pagina a cargar
     * @return Lista de peliculas de la API
     */
    suspend fun fetchMovies(page: Int): List<Item> {
        val response = apiService.getMovies(API_KEY, page)
        return response.results
    }

    /**
     * Metodo que busca peliculas a partir de una busqueda y pagina
     * @param query Consulta para encontrar una pelicula
     * @param page Pagina a cargar
     * @return Lista de peliculas a partir de la consulta
     */
    suspend fun searchMovies(query: String, page: Int): List<Item> {
        val response = apiService.searchMovies(API_KEY, query, page)
        return response.results
    }
}
