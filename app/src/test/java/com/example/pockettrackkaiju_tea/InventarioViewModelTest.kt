package com.example.pockettrackkaiju_tea

import com.example.pockettrackkaiju_tea.model.Producto
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

    private fun productoDePrueba(codigo: String, precio: Int = 1000) = Producto(
        codigo = codigo,
        nombre = "Producto de prueba",
        descripcion = "",
        categoria = "Abarrotes",
        tipo = "Alimento",
        precio = precio,
        stockActual = 5,
        stockMinimo = 2
    )

    @Test
    fun productoNuevoApareceEnElCatalogo() {
        assertTrue(viewModel.agregarProducto(productoDePrueba("P-999")))
        assertEquals(11, viewModel.productos.value.size)
    }

    @Test
    fun rechazaCodigoRepetido() {
        assertFalse(viewModel.agregarProducto(productoDePrueba("P-001")))
        assertEquals(10, viewModel.productos.value.size)
    }

    @Test
    fun rechazaPrecioCero() {
        assertFalse(viewModel.agregarProducto(productoDePrueba("P-998", precio = 0)))
    }
}