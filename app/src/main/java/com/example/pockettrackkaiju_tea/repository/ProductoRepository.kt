package com.example.pockettrackkaiju_tea.repository

import com.example.pockettrackkaiju_tea.database.dao.ProductoDao
import com.example.pockettrackkaiju_tea.model.Producto
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val dao: ProductoDao) {

    val productos: Flow<List<Producto>> = dao.obtenerTodos()

    suspend fun guardar(producto: Producto) = dao.insertar(producto)

    suspend fun actualizar(producto: Producto) = dao.actualizar(producto)

    suspend fun eliminar(producto: Producto) = dao.eliminar(producto)
}