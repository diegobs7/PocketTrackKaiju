package com.example.pockettrackkaiju_tea


import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.model.TipoMovimiento
import com.example.pockettrackkaiju_tea.repository.MovimientoRepository
import com.example.pockettrackkaiju_tea.repository.ProductoRepository
import com.example.pockettrackkaiju_tea.viewmodel.MovimientoViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovimientoViewModelTest {

    @get:Rule
    val reglaMain = ReglaDispatcherPrincipal()

    private lateinit var productoDao: FakeProductoDao
    private lateinit var movimientoDao: FakeMovimientoDao
    private lateinit var viewModel: MovimientoViewModel
    private lateinit var producto: Producto

    private val responsable = usuarioDePrueba()

    @Before
    fun preparar() {
        productoDao = FakeProductoDao()
        movimientoDao = FakeMovimientoDao()
        // Producto con stock 10 para todos los tests.
        producto = productoDao.agregarDirecto(productoDePrueba("KAI-001", stockActual = 10))
        viewModel = MovimientoViewModel(
            ProductoRepository(productoDao),
            MovimientoRepository(movimientoDao)
        )
    }

    private fun completarFormulario(
        tipo: TipoMovimiento,
        cantidad: String,
        motivo: String = "Motivo de prueba"
    ) {
        viewModel.seleccionarProducto(producto)
        viewModel.seleccionarTipo(tipo)
        viewModel.actualizarCantidad(cantidad)
        viewModel.actualizarMotivo(motivo)
    }

    private fun stockActual(): Int = productoDao.lista.single().stockActual

    // ---------- Validaciones ----------

    @Test
    fun formularioVacioMarcaTodosLosCamposObligatorios() {
        assertFalse(viewModel.validarFormulario())

        val errores = viewModel.estadoFormulario.value.errores
        assertNotNull(errores.errorProducto)
        assertNotNull(errores.errorTipo)
        assertNotNull(errores.errorCantidad)
        assertNotNull(errores.errorMotivo)
    }

    @Test
    fun formularioCorrectoNoTieneErrores() {
        completarFormulario(TipoMovimiento.ENTRADA, "5")

        assertTrue(viewModel.validarFormulario())
    }

    @Test
    fun cantidadCeroEnUnaEntradaEsInvalida() {
        completarFormulario(TipoMovimiento.ENTRADA, "0")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorCantidad)
    }

    @Test
    fun cantidadNegativaEnUnaVentaEsInvalida() {
        completarFormulario(TipoMovimiento.SALIDA_VENTA, "-2")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorCantidad)
    }

    @Test
    fun cantidadQueNoEsNumeroEsInvalida() {
        completarFormulario(TipoMovimiento.ENTRADA, "diez")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorCantidad)
    }

    @Test
    fun unAjusteNoPuedeSerCero() {
        completarFormulario(TipoMovimiento.AJUSTE, "0")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorCantidad)
    }

    @Test
    fun motivoEnBlancoEsInvalido() {
        completarFormulario(TipoMovimiento.ENTRADA, "5", motivo = "   ")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorMotivo)
    }

    @Test
    fun noSePuedeSacarMasStockDelQueHay() {
        completarFormulario(TipoMovimiento.SALIDA_VENTA, "11")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorCantidad)
    }

    @Test
    fun sePuedeSacarExactamenteTodoElStock() {
        completarFormulario(TipoMovimiento.SALIDA_CONSUMO, "10")

        assertTrue(viewModel.registrarMovimiento(responsable))

        assertEquals(0, stockActual())
    }

    // ---------- Registrar movimientos ----------

    @Test
    fun unaEntradaSumaAlStockYGuardaElMovimiento() {
        completarFormulario(TipoMovimiento.ENTRADA, "5")

        assertTrue(viewModel.registrarMovimiento(responsable))

        assertEquals(15, stockActual())
        assertEquals(1, movimientoDao.lista.size)
    }

    @Test
    fun unaVentaRestaDelStock() {
        completarFormulario(TipoMovimiento.SALIDA_VENTA, "4")

        viewModel.registrarMovimiento(responsable)

        assertEquals(6, stockActual())
    }

    @Test
    fun unAjusteNegativoRestaDelStock() {
        completarFormulario(TipoMovimiento.AJUSTE, "-3")

        assertTrue(viewModel.registrarMovimiento(responsable))

        assertEquals(7, stockActual())
    }

    @Test
    fun elMovimientoQuedaConElResponsableYElProducto() {
        completarFormulario(TipoMovimiento.ENTRADA, "5", motivo = "Reposición de bodega")

        viewModel.registrarMovimiento(responsable)

        val guardado = movimientoDao.lista.single()
        assertEquals("KAI-001", guardado.codigoProducto)
        assertEquals(responsable.nombre, guardado.responsable)
        assertEquals(responsable.id, guardado.responsableId)
        assertEquals("Reposición de bodega", guardado.motivo)
        assertEquals(TipoMovimiento.ENTRADA, guardado.tipo)
    }

    @Test
    fun unMovimientoInvalidoNoCambiaElStockNiGuardaNada() {
        completarFormulario(TipoMovimiento.SALIDA_VENTA, "50")

        assertFalse(viewModel.registrarMovimiento(responsable))

        assertEquals(10, stockActual())
        assertTrue(movimientoDao.lista.isEmpty())
    }

    @Test
    fun alRegistrarElFormularioSeLimpia() {
        completarFormulario(TipoMovimiento.ENTRADA, "5")

        viewModel.registrarMovimiento(responsable)

        assertNull(viewModel.estadoFormulario.value.producto)
        assertEquals("", viewModel.estadoFormulario.value.cantidad)
    }
}
