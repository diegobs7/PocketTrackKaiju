package com.example.pockettrackkaiju_tea.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pockettrackkaiju_tea.database.dao.MovimientoDao
import com.example.pockettrackkaiju_tea.database.dao.ProductoDao
import com.example.pockettrackkaiju_tea.database.dao.UsuarioDao
import com.example.pockettrackkaiju_tea.model.Movimiento
import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.model.Usuario

@Database(
    entities = [Producto::class, Usuario::class, Movimiento::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun movimientoDao(): MovimientoDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null

        fun getInstance(contexto: Context): AppDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    contexto.applicationContext,
                    AppDatabase::class.java,
                    "pockettrack_db"
                ).build().also { instancia = it }
            }
    }
}