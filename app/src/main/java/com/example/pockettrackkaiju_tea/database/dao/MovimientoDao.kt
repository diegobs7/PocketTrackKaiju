package com.example.pockettrackkaiju_tea.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pockettrackkaiju_tea.model.Movimiento
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientoDao {

    @Query("SELECT * FROM movimientos ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<Movimiento>>

    @Query("SELECT * FROM movimientos WHERE codigoProducto = :codigo ORDER BY id DESC")
    fun obtenerPorProducto(codigo: String): Flow<List<Movimiento>>

    @Insert
    suspend fun insertar(movimiento: Movimiento)
}