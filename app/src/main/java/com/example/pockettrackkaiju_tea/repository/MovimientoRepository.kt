package com.example.pockettrackkaiju_tea.repository

import com.example.pockettrackkaiju_tea.database.dao.MovimientoDao
import com.example.pockettrackkaiju_tea.model.Movimiento
import kotlinx.coroutines.flow.Flow

class MovimientoRepository(private val dao: MovimientoDao) {

    val movimientos: Flow<List<Movimiento>> = dao.obtenerTodos()

    fun movimientosDe(codigo: String): Flow<List<Movimiento>> = dao.obtenerPorProducto(codigo)

    suspend fun guardar(movimiento: Movimiento) = dao.insertar(movimiento)
}