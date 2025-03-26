package com.example.tmdb.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Esta clase es la base de datos local de la aplicacion mediante Room
 *
 * @property movieDao Data Access Object para hacer operacione en la bd
 */
@Database(entities = [MovieEntity::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Crea instancia de la bd, y si no hay una la crea
         *
         * @param context Conexto de la aplicacion
         * @return La instancia de esta
         */

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movie_db"
                )
                    .addMigrations(
                        MIGRATION_2_3,
                        MIGRATION_4_5
                    )
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * Migracion de la 2 a la 3
         * Renombra dos columnas, posterPath a poster_path y releaseDate a release_date
         */
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE favorite_movie RENAME COLUMN posterPath TO poster_path")
                database.execSQL("ALTER TABLE favorite_movie RENAME COLUMN releaseDate TO release_date")
            }
        }

        //No esta la migracion 3 a la 4 porque al final no utilizo ninguno de los cambios que hice

        /**
         * Migracion de la 4 a la 5 donde cambio la declaracion de algunos atributos
         */
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //Renombrar la tabla actual a "old_favorite_movie"
                database.execSQL("ALTER TABLE favorite_movie RENAME TO old_favorite_movie")

                //Crear la nueva tabla con la estructura actualizada
                database.execSQL(
                    """
            CREATE TABLE favorite_movie (
                id INTEGER PRIMARY KEY NOT NULL,
                title TEXT NOT NULL,
                poster_path TEXT NOT NULL,
                release_date TEXT NOT NULL,
                adult INTEGER NOT NULL,
                backdrop_path TEXT NOT NULL,
                original_language TEXT NOT NULL,
                overview TEXT NOT NULL,
                popularity REAL NOT NULL,
                vote_average REAL NOT NULL
            )
            """.trimIndent()
                )

                //Copiar los datos de la tabla antigua a la nueva
                database.execSQL(
                    """
            INSERT INTO favorite_movie (id, title, poster_path, release_date, adult, backdrop_path, original_language, overview, popularity, vote_average)
            SELECT id, title, poster_path, release_date, adult, backdrop_path, original_language, overview, popularity, vote_average
            FROM old_favorite_movie
            WHERE title IS NOT NULL AND poster_path IS NOT NULL AND release_date IS NOT NULL
            AND adult IS NOT NULL AND backdrop_path IS NOT NULL AND original_language IS NOT NULL
            AND overview IS NOT NULL
            """.trimIndent()
                )

                //Eliminar la tabla antigua
                database.execSQL("DROP TABLE old_favorite_movie")
            }
        }
    }
}
