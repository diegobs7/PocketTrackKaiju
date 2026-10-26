package com.example.pockettrackkaiju_tea.repository

import com.example.pockettrackkaiju_tea.database.dao.ProductoDao
import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.util.ProductosDePrueba
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ProductoRepository(private val dao: ProductoDao) {

    val productos: Flow<List<Producto>> = dao.obtenerTodos()

    suspend fun guardar(producto: Producto) = dao.insertar(producto)

    suspend fun actualizar(producto: Producto) = dao.actualizar(producto)

    suspend fun eliminar(producto: Producto) = dao.eliminar(producto)

    suspend fun cargarProductosDePrueba() {
        if (dao.obtenerTodos().first().isNotEmpty()) return
        ProductosDePrueba.lista.forEach { dao.insertar(it) }
    }
}