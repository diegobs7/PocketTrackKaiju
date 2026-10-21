package com.example.pockettrackkaiju_tea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockettrackkaiju_tea.model.Movimiento
import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.repository.MovimientoRepository
import com.example.pockettrackkaiju_tea.repository.ProductoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class InventarioViewModel(
    productoRepository: ProductoRepository,
    movimientoRepository: MovimientoRepository
) : ViewModel() {

    val productos: StateFlow<List<Producto>> = productoRepository.productos
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val movimientos: StateFlow<List<Movimiento>> = movimientoRepository.movimientos
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun buscarProducto(codigo: String): Producto? =
        productos.value.find { it.codigo.equals(codigo, ignoreCase = true) }

    fun estaEnAlerta(producto: Producto): Boolean =
        producto.stockActual <= producto.stockMinimo

    fun movimientosDe(codigo: String): List<Movimiento> =
        movimientos.value.filter { it.codigoProducto.equals(codigo, ignoreCase = true) }
}