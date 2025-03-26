package com.example.tmdb.api

import com.example.tmdb.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz para la API de películas.
 */
interface MovieAPIService {

    /**
     * EndPoint que devuelve las películas populares.
     *
     * @param apiKey Clave API para autenticar.
     * @param page Página a cargar.
     * @return Objeto MovieResponse con las películas a partir de estos parámetros.
     */
    @GET("movie/popular")
    suspend fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): MovieResponse

    /**
     * EndPoint que busca películas a partir de una búsqueda.
     *
     * @param apiKey Clave API para autenticar.
     * @param query Consulta para encontrar la película por su título.
     * @param page Página a cargar.
     * @return Objeto MovieResponse con la búsqueda de películas.
     */
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): MovieResponse
}