package com.example.tmdb.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Dao que contiene las siguientes contultas
 * - Obtiene todas las peliculas agregadas a favoritos
 * - Inserta una pelicula a favoritos
 * - Elimina una pelicula de favoritos
 */
@Dao
interface MovieDao {

    //Devuelve la lista de favoritos
    @Query("Select * From favorite_movie")
    fun getAllFavorites(): Flow<List<MovieEntity>>

    //Añade una pelicula a favoritos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie:MovieEntity)

    //Elimina una pelicula de favoritos
    @Delete
    suspend fun deleteFavorite(movie: MovieEntity)


}