package com.example.pockettrackkaiju_tea.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pockettrackkaiju_tea.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuarios ORDER BY nombre ASC")
    fun obtenerTodos(): Flow<List<Usuario>>

    @Insert
    suspend fun insertar(usuario: Usuario)

    @Delete
    suspend fun eliminar(usuario: Usuario)
    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int
}