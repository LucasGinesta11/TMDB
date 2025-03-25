package com.example.proyecto.room.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tmdb.room.MovieDao
import com.example.tmdb.room.MovieEntity

/**
 * Clase que representa la base de datos de Room para la aplicación.
 *
 * Esta clase es responsable de crear y gestionar la base de datos de películas,
 * así como de proporcionar acceso a los Data Access Objects (DAO) definidos en la aplicación.
 *
 * @property movieDao DAO para acceder a las operaciones relacionadas con las películas favoritas.
 */
@Database(entities = [MovieEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Obtiene la instancia del [MovieDao] para realizar operaciones de base de datos.
     *
     * @return [MovieDao] para acceder a las películas favoritas.
     */
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia de la base de datos, creando una si no existe.
         *
         * Este método utiliza el patrón Singleton para asegurar que solo haya
         * una instancia de la base de datos en toda la aplicación.
         *
         * @param context El contexto de la aplicación.
         * @return La instancia de [AppDatabase].
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movie_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * Migración de la base de datos de la versión 1 a la versión 2.
         *
         * No se requieren cambios en la estructura de la base de datos para esta migración,
         * por lo que el método está vacío.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // No se necesitan cambios ya que no hay nuevas tablas o columnas
            }
        }
    }
}
