package com.example.segunda_entrega.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.segunda_entrega.dao.ProductoDAO
import com.example.segunda_entrega.dao.UsuarioDAO
import com.example.segunda_entrega.modelo.Usuario
import com.example.segunda_entrega.modelo.CrearProducto

@Database(entities = [Usuario::class, CrearProducto::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UsuarioDAO
    abstract fun productoDao(): ProductoDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration() // Usar esta opción si no te importa perder datos
                    .allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}


