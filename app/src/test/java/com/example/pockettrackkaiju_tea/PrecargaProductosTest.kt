package com.example.pockettrackkaiju_tea

import com.example.pockettrackkaiju_tea.repository.MovimientoRepository
import com.example.pockettrackkaiju_tea.repository.ProductoRepository
import com.example.pockettrackkaiju_tea.util.ProductosDePrueba
import com.example.pockettrackkaiju_tea.viewmodel.InventarioViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class PrecargaProductosTest {

    @get:Rule
    val reglaMain = ReglaDispatcherPrincipal()

    @Test
    fun conElCatalogoVacioSeCarganLosProductosDePrueba() {
        val productoDao = FakeProductoDao()

        InventarioViewModel(
            ProductoRepository(productoDao),
            MovimientoRepository(FakeMovimientoDao())
        )

        assertEquals(ProductosDePrueba.lista.size, productoDao.lista.size)
    }

    @Test
    fun siYaHayProductosNoSeAgregaNada() {
        val productoDao = FakeProductoDao()
        productoDao.agregarDirecto(productoDePrueba("KAI-001"))

        InventarioViewModel(
            ProductoRepository(productoDao),
            MovimientoRepository(FakeMovimientoDao())
        )

        assertEquals(1, productoDao.lista.size)
    }

    @Test
    fun losCodigosDeLosProductosDePruebaNoSeRepiten() {
        val codigos = ProductosDePrueba.lista.map { it.codigo }

        assertEquals(codigos.size, codigos.distinct().size)
    }
}