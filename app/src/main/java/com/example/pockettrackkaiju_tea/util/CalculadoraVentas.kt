package com.example.pockettrackkaiju_tea.util

import com.example.pockettrackkaiju_tea.model.Movimiento
import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.model.TipoMovimiento
import com.example.pockettrackkaiju_tea.model.VentaCategoria
import com.example.pockettrackkaiju_tea.model.VentaProducto

object CalculadoraVentas {

    fun ventasPorProducto(
        productos: List<Producto>,
        movimientos: List<Movimiento>
    ): List<VentaProducto> {
        val unidadesPorCodigo = movimientos
            .filter { it.tipo == TipoMovimiento.SALIDA_VENTA }
            .groupBy { it.codigoProducto }
            .mapValues { (_, lista) -> lista.sumOf { it.cantidad } }

        return productos
            .map { VentaProducto(it, unidadesPorCodigo[it.codigo] ?: 0) }
            .sortedByDescending { it.unidades }
    }

    fun ventasPorCategoria(ventas: List<VentaProducto>): List<VentaCategoria> =
        ventas
            .groupBy { it.producto.categoria }
            .map { (categoria, lista) -> VentaCategoria(categoria, lista.sumOf { it.unidades }) }
            .sortedByDescending { it.unidades }
}