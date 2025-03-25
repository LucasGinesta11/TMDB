package com.example.tmdb.room

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_movie")
data class MovieEntity(
    @PrimaryKey val id:Int,
    val title:String,
    val posterPath:String?,
    val releaseDate:String,
    val adult:Boolean,
    val backdrop_path:String?,
    val original_language:String?,
    val overview:String?,
    val popularity:Double,
    val vote_average:Double
)