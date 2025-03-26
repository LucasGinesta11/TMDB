package com.example.tmdb.model

/**
 * Clase que representa una película.
 *
 * @property id Identificador único de la película.
 * @property adult Indica si la película es para adultos.
 * @property original_language Idioma original de la película.
 * @property backdrop_path Ruta de la imagen de fondo de la película.
 * @property title Título de la película.
 * @property release_date Fecha de lanzamiento de la película.
 * @property overview Descripción breve de la película.
 * @property vote_average Puntuación promedio de la película.
 * @property popularity Popularidad de la película.
 */
data class Item(
    val id: Int,
    val adult: Boolean,
    val original_language: String?,
    val backdrop_path: String?,
    val title: String,
    val release_date: String,
    val overview: String?,
    val vote_average: Double,
    val popularity: Double,
    val poster_path: String?
)

/**
 * Clase que representa la respuesta de la API que contiene un conjunto de películas.
 *
 * @property page Página actual de la respuesta.
 * @property results Lista de películas en la página.
 */
data class MovieResponse(
    val page: Int,
    val results: List<Item>
)

/**
 * Clase que representa una película con atributos adicionales para la aplicación.
 *
 * @property id Identificador único de la película.
 * @property adult Indica si la película es para adultos.
 * @property originalLanguage Idioma original de la película.
 * @property backdropPath Ruta de la imagen de fondo de la película.
 * @property title Título de la película.
 * @property releaseDate Fecha de lanzamiento de la película.
 * @property overview Descripción breve de la película.
 * @property voteAverage Puntuación promedio de la película.
 * @property popularity Popularidad de la película.
 * @property originalTitle Título original de la película.
 * @property posterPath Ruta de la imagen del cartel de la película.
 * @property video Indica si la película tiene contenido en video asociado.
 * @property genreIds Lista de identificadores de géneros de la película.
 */
data class Movie(
    val id: Int,
    val adult: Boolean,
    val originalLanguage: String?,
    val backdropPath: String?,
    val title: String,
    val releaseDate: String,
    val overview: String?,
    val voteAverage: Double,
    val popularity: Double,
    val originalTitle: String?,
    val posterPath: String?,
    val video: Boolean,
    val genreIds: List<Int>
)

/**
 * Extensión para convertir un objeto [Item] (API) a [Movie] (App).
 *
 * @return Un objeto [Movie] con los atributos correspondientes.
 */
fun Item.toMovie(): Movie {
    return Movie(
        id = id,
        adult = adult,
        originalLanguage = original_language,
        backdropPath = backdrop_path,
        title = title,
        releaseDate = release_date,
        overview = overview,
        voteAverage = vote_average,
        popularity = popularity,
        originalTitle = null, // Establecer a null o proporcionar un valor adecuado
        posterPath = null, // Establecer a null o proporcionar un valor adecuado
        video = false, // Establecer a false o proporcionar un valor adecuado
        genreIds = emptyList() // Proporcionar una lista vacía o los géneros correspondientes
    )
}
