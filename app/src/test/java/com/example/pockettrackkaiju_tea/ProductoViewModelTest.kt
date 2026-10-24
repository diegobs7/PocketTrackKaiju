package com.example.pockettrackkaiju_tea


import com.example.pockettrackkaiju_tea.model.CategoriaProducto
import com.example.pockettrackkaiju_tea.model.TipoProducto
import com.example.pockettrackkaiju_tea.repository.ProductoRepository
import com.example.pockettrackkaiju_tea.viewmodel.ProductoViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductoViewModelTest {

    @get:Rule
    val reglaMain = ReglaDispatcherPrincipal()

    private lateinit var productoDao: FakeProductoDao
    private lateinit var viewModel: ProductoViewModel

    @Before
    fun preparar() {
        productoDao = FakeProductoDao()
        viewModel = ProductoViewModel(ProductoRepository(productoDao))
    }

    // Llena el formulario con datos válidos; cada test cambia solo el campo que quiere probar.
    private fun completarFormularioValido(codigo: String = "KAI-100") {
        viewModel.actualizarCodigo(codigo)
        viewModel.actualizarNombre("Polera Mothra")
        viewModel.actualizarDescripcion("Polera negra con estampado de Mothra")
        viewModel.actualizarCategoria(CategoriaProducto.ASIAN_KAIJU.displayName)
        viewModel.actualizarTipo(TipoProducto.POLERA.displayName)
        viewModel.actualizarPrecio("15990")
        viewModel.actualizarStockActual("10")
        viewModel.actualizarStockMinimo("3")
    }

    // ---------- Validaciones ----------

    @Test
    fun formularioVacioMarcaLosCamposObligatorios() {
        assertFalse(viewModel.validarFormulario())

        val errores = viewModel.estadoFormulario.value.errores
        assertNotNull(errores.errorCodigo)
        assertNotNull(errores.errorNombre)
        assertNotNull(errores.errorDescripcion)
        assertNotNull(errores.errorCategoria)
        assertNotNull(errores.errorTipo)
        assertNotNull(errores.errorPrecio)
        assertNotNull(errores.errorStockActual)
        assertNotNull(errores.errorStockMinimo)
    }

    @Test
    fun formularioCorrectoNoTieneErrores() {
        completarFormularioValido()

        assertTrue(viewModel.validarFormulario())
    }

    @Test
    fun precioQueNoEsNumeroEsInvalido() {
        completarFormularioValido()
        viewModel.actualizarPrecio("quince mil")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorPrecio)
    }

    @Test
    fun precioCeroEsInvalido() {
        completarFormularioValido()
        viewModel.actualizarPrecio("0")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorPrecio)
    }

    @Test
    fun stockNegativoEsInvalido() {
        completarFormularioValido()
        viewModel.actualizarStockActual("-1")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorStockActual)
    }

    @Test
    fun stockMinimoNegativoEsInvalido() {
        completarFormularioValido()
        viewModel.actualizarStockMinimo("-2")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorStockMinimo)
    }

    @Test
    fun stockCeroSiEsValido() {
        completarFormularioValido()
        viewModel.actualizarStockActual("0")

        assertTrue(viewModel.validarFormulario())
    }

    // ---------- Crear producto ----------

    @Test
    fun crearProductoValidoLoGuardaConSusDatos() {
        completarFormularioValido()

        assertTrue(viewModel.crearProducto())

        val guardado = productoDao.lista.single()
        assertEquals("KAI-100", guardado.codigo)
        assertEquals(15990, guardado.precio)
        assertEquals(10, guardado.stockActual)
        assertEquals(3, guardado.stockMinimo)
    }

    @Test
    fun laCategoriaYElTipoDelFormularioSeConviertenAEnum() {
        completarFormularioValido()

        viewModel.crearProducto()

        val guardado = productoDao.lista.single()
        assertEquals(CategoriaProducto.ASIAN_KAIJU, guardado.categoria)
        assertEquals(TipoProducto.POLERA, guardado.tipo)
    }

    @Test
    fun elDetalleEnBlancoSeGuardaComoNulo() {
        completarFormularioValido()
        viewModel.actualizarDetalle("   ")

        viewModel.crearProducto()

        assertNull(productoDao.lista.single().detalle)
    }

    @Test
    fun alCrearElProductoElFormularioSeLimpia() {
        completarFormularioValido()

        viewModel.crearProducto()

        assertEquals("", viewModel.estadoFormulario.value.codigo)
        assertEquals("", viewModel.estadoFormulario.value.nombre)
    }

    @Test
    fun noSePuedeCrearUnProductoConCodigoRepetido() {
        completarFormularioValido("KAI-100")
        viewModel.crearProducto()

        // Mismo código escrito en minúsculas: también cuenta como repetido.
        completarFormularioValido("kai-100")

        assertFalse(viewModel.crearProducto())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorCodigo)
        assertEquals(1, productoDao.lista.size)
    }

    @Test
    fun crearConFormularioInvalidoNoGuardaNada() {
        completarFormularioValido()
        viewModel.actualizarPrecio("0")

        assertFalse(viewModel.crearProducto())

        assertTrue(productoDao.lista.isEmpty())
    }

    // ---------- Editar producto ----------

    @Test
    fun cargarParaEditarLlenaElFormularioConLosDatosDelProducto() {
        completarFormularioValido()
        viewModel.crearProducto()

        viewModel.cargarParaEditar(productoDao.lista.single().id)

        val formulario = viewModel.estadoFormulario.value
        assertEquals("KAI-100", formulario.codigo)
        assertEquals("Polera Mothra", formulario.nombre)
        assertEquals("15990", formulario.precio)
    }

    @Test
    fun guardarCambiosActualizaElProductoSinDarloPorRepetido() {
        completarFormularioValido()
        viewModel.crearProducto()
        viewModel.cargarParaEditar(productoDao.lista.single().id)
        viewModel.actualizarPrecio("19990")

        // Al editar, su propio código no cuenta como "repetido".
        assertTrue(viewModel.guardarCambios())

        assertEquals(19990, productoDao.lista.single().precio)
    }

    @Test
    fun editarUnProductoNoCambiaSuStock() {
        // El stock solo se modifica registrando movimientos, nunca desde el formulario de edición.
        completarFormularioValido()
        viewModel.crearProducto()
        viewModel.cargarParaEditar(productoDao.lista.single().id)
        viewModel.actualizarStockActual("999")

        viewModel.guardarCambios()

        assertEquals(10, productoDao.lista.single().stockActual)
    }

    @Test
    fun guardarCambiosSinHaberCargadoUnProductoDevuelveFalso() {
        completarFormularioValido()

        assertFalse(viewModel.guardarCambios())
    }
}
