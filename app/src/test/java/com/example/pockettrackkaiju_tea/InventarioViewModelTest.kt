package com.example.pockettrackkaiju_tea

import com.example.pockettrackkaiju_tea.viewmodel.InventarioViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class InventarioViewModelTest {

    private val viewModel = InventarioViewModel()

    @Test
    fun catalogoTiene10Productos() {
        assertEquals(10, viewModel.productos.value.size)
    }

    @Test
    fun buscarProductoPorCodigoIgnoraMayusculas() {
        assertNotNull(viewModel.buscarProducto("p-001"))
    }

    @Test
    fun alertaCuandoStockEsMenorOIgualAlMinimo() {
        // P-002: stock 3, mínimo 8 → en alerta · P-001: stock 25, mínimo 10 → no
        assertTrue(viewModel.estaEnAlerta(viewModel.buscarProducto("P-002")!!))
        assertFalse(viewModel.estaEnAlerta(viewModel.buscarProducto("P-001")!!))
    }

    @Test
    fun historialDeUnProductoSoloTraeSusMovimientos() {
        assertEquals(1, viewModel.movimientosDe("P-002").size)
    }
}