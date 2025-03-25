package com.example.tmdb.model

/**
 * Clase que representa una película.
 *
 * @property id Identificador único de la película.
 * @property title Título de la película.
 * @property posterPath Ruta de la imagen del cartel de la película.
 * @property releaseDate Fecha de lanzamiento de la película.
 * @property overview Descripción breve de la película.
 * @property voteAverage Puntuación promedio de la película.
 * @property popularity Popularidad de la película.
 */
data class Item(
    val id: Int,
    val adult: Boolean,
    val original_language: String?,
    val backdrop_path: String?,
    val title: String,                 // Título de la película
    val poster_path: String?,           // Ruta de la imagen del cartel de la película (puede ser nulo)
    val release_date: String,          // Fecha de lanzamiento de la película (puede ser nulo)
    val overview: String?,              // Descripción breve de la película (puede ser nulo)
    val vote_average: Double,           // Puntuación promedio de la película
    val popularity: Double              // Popularidad de la película
)

data class Movie(val page:Int, val results: List<Item>)
