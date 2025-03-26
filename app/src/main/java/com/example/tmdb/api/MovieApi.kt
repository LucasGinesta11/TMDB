package com.example.tmdb.api

import com.example.tmdb.model.Item
import com.example.tmdb.model.MovieResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton (única instancia) que da acceso a la API.
 * Uso de Retrofit para realizar solicitudes HTTP.
 */
object MovieAPI {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val API_KEY = "13a30b1187404441e8327c0a2d58dc3f"

    /**
     * Lazy (se inicia cuando se llama por primera vez).
     * Build de Retrofit a partir de la URL.
     */
    private val apiService: MovieAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieAPIService::class.java)
    }

    /**
     * Método que devuelve una lista de películas a partir de su página.
     * @param page Página a cargar.
     * @return Lista de películas de la API.
     */
    suspend fun fetchMovies(page: Int): List<Item> {
        val response: MovieResponse = apiService.getMovies(API_KEY, page)
        return response.results
    }

    /**
     * Método que busca películas a partir de una búsqueda y página.
     * @param query Consulta para encontrar una película.
     * @param page Página a cargar.
     * @return Lista de películas a partir de la consulta.
     */
    suspend fun searchMovies(query: String, page: Int): List<Item> {
        val response: MovieResponse = apiService.searchMovies(API_KEY, query, page)
        return response.results
    }
}
