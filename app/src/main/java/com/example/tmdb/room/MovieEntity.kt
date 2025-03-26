package com.example.tmdb.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de la base de datos que contiene los mismo atributos que el Model
 */
@Entity(tableName = "favorite_movie")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val poster_path: String,
    val release_date: String,
    val adult: Boolean,
    val backdrop_path: String,
    val original_language: String,
    val overview: String,
    val popularity: Double,
    val vote_average: Double
)
