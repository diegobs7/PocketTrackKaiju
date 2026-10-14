package com.example.pockettrackkaiju_tea.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pockettrackkaiju_tea.model.Movimiento
import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.repository.InventarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InventarioViewModel : ViewModel() {

    private val repositorio = InventarioRepository()

    private val _productos = MutableStateFlow(repositorio.obtenerProductos())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    fun buscarProducto(codigo: String): Producto? =
        _productos.value.find { it.codigo.equals(codigo, ignoreCase = true) }

    fun estaEnAlerta(producto: Producto): Boolean =
        producto.stockActual <= producto.stockMinimo

    fun movimientosDe(codigo: String): List<Movimiento> =
        repositorio.obtenerMovimientos()
            .filter { it.codigoProducto.equals(codigo, ignoreCase = true) }
            .sortedByDescending { it.fecha }
}