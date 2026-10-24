package com.example.pockettrackkaiju_tea

import com.example.pockettrackkaiju_tea.model.TipoMovimiento
import com.example.pockettrackkaiju_tea.repository.MovimientoRepository
import com.example.pockettrackkaiju_tea.repository.ProductoRepository
import com.example.pockettrackkaiju_tea.viewmodel.InventarioViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InventarioViewModelTest {

    @get:Rule
    val reglaMain = ReglaDispatcherPrincipal()

    private lateinit var productoDao: FakeProductoDao
    private lateinit var movimientoDao: FakeMovimientoDao
    private lateinit var viewModel: InventarioViewModel

    @Before
    fun preparar() {
        productoDao = FakeProductoDao()
        movimientoDao = FakeMovimientoDao()
        // KAI-001: stock 25, mínimo 10 (sin alerta) · KAI-002: stock 4, mínimo 5 (en alerta)
        productoDao.agregarDirecto(productoDePrueba("KAI-001", stockActual = 25, stockMinimo = 10))
        productoDao.agregarDirecto(productoDePrueba("KAI-002", stockActual = 4, stockMinimo = 5))
        viewModel = InventarioViewModel(
            ProductoRepository(productoDao),
            MovimientoRepository(movimientoDao)
        )
    }

    @Test
    fun catalogoMuestraLosProductosGuardados() {
        assertEquals(2, viewModel.productos.value.size)
    }

    @Test
    fun buscarProductoIgnoraMayusculas() {
        assertNotNull(viewModel.buscarProducto("kai-001"))
    }

    @Test
    fun buscarProductoInexistenteDevuelveNull() {
        assertNull(viewModel.buscarProducto("NO-EXISTE"))
    }

    @Test
    fun hayAlertaCuandoElStockEstaBajoElMinimo() {
        assertTrue(viewModel.estaEnAlerta(viewModel.buscarProducto("KAI-002")!!))
    }

    @Test
    fun noHayAlertaCuandoElStockSuperaElMinimo() {
        assertFalse(viewModel.estaEnAlerta(viewModel.buscarProducto("KAI-001")!!))
    }

    @Test
    fun hayAlertaCuandoElStockEsIgualAlMinimo() {
        // Caso límite: stock 5 y mínimo 5 → la regla es "menor o igual", así que sí alerta.
        assertTrue(viewModel.estaEnAlerta(productoDePrueba(stockActual = 5, stockMinimo = 5)))
    }

    @Test
    fun movimientosDeUnProductoSoloTraeLosSuyos() {
        movimientoDao.agregarDirecto(movimientoDePrueba("KAI-001"))
        movimientoDao.agregarDirecto(movimientoDePrueba("KAI-001", TipoMovimiento.SALIDA_VENTA, 2))
        movimientoDao.agregarDirecto(movimientoDePrueba("KAI-002"))

        assertEquals(2, viewModel.movimientosDe("kai-001").size)
    }

    @Test
    fun eliminarProductoSinMovimientosLoQuitaDelCatalogo() {
        viewModel.eliminarProducto(viewModel.buscarProducto("KAI-001")!!)

        assertNull(viewModel.buscarProducto("KAI-001"))
        assertEquals(1, productoDao.lista.size)
    }

    @Test
    fun noSePuedeEliminarUnProductoConMovimientos() {
        movimientoDao.agregarDirecto(movimientoDePrueba("KAI-001"))

        viewModel.eliminarProducto(viewModel.buscarProducto("KAI-001")!!)

        assertNotNull(viewModel.buscarProducto("KAI-001"))
        assertEquals(2, productoDao.lista.size)
    }
}
