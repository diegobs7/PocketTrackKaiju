package com.example.pockettrackkaiju_tea

import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.repository.UsuarioRepositoryEnMemoria
import com.example.pockettrackkaiju_tea.viewmodel.EditarUsuarioViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditarUsuarioViewModelTest {

    @get:Rule
    val reglaMain = ReglaDispatcherPrincipal()

    private lateinit var repositorio: UsuarioRepositoryEnMemoria
    private lateinit var viewModel: EditarUsuarioViewModel

    // En el repositorio en memoria: 1 = Thomas, 2 = Andrés, 3 = Diego.
    private val idThomas = 1

    @Before
    fun preparar() {
        repositorio = UsuarioRepositoryEnMemoria()
        viewModel = EditarUsuarioViewModel(repositorio)
    }

    private fun usuarioGuardado(id: Int) =
        runBlocking { repositorio.listar() }.first { it.id == id }

    @Test
    fun alCargarUnUsuarioElFormularioQuedaConSusDatos() {
        viewModel.cargarUsuario(idThomas)

        val estado = viewModel.estadoFormulario.value
        assertEquals("Thomas Araya", estado.nombre)
        assertEquals("thomas@kaiju.cl", estado.email)
        assertEquals(Rol.VENDEDOR, estado.rol)
    }

    @Test
    fun cargarUnUsuarioInexistenteMuestraUnErrorGeneral() {
        viewModel.cargarUsuario(99)

        assertNotNull(viewModel.estadoFormulario.value.errores.errorGeneral)
    }

    @Test
    fun nombreConMenosDe3CaracteresEsInvalido() {
        viewModel.cargarUsuario(idThomas)
        viewModel.actualizarNombre("Al")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorNombre)
    }

    @Test
    fun correoSinFormatoEsInvalido() {
        viewModel.cargarUsuario(idThomas)
        viewModel.actualizarEmail("esto-no-es-un-correo")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorEmail)
    }

    @Test
    fun guardarActualizaElNombreYElCorreoEnMinusculas() {
        viewModel.cargarUsuario(idThomas)
        viewModel.actualizarNombre("Thomas Araya Soto")
        viewModel.actualizarEmail("  Thomas.Nuevo@Kaiju.CL ")

        var guardado = false
        viewModel.guardarCambios { guardado = true }

        assertTrue(guardado)
        val usuario = usuarioGuardado(idThomas)
        assertEquals("Thomas Araya Soto", usuario.nombre)
        assertEquals("thomas.nuevo@kaiju.cl", usuario.correo)
    }

    @Test
    fun guardarConElCorreoDeOtroUsuarioMuestraErrorYNoGuarda() {
        viewModel.cargarUsuario(idThomas)
        viewModel.actualizarEmail("diego@kaiju.cl")

        var guardado = false
        viewModel.guardarCambios { guardado = true }

        assertFalse(guardado)
        assertNotNull(viewModel.estadoFormulario.value.errores.errorEmail)
        assertFalse(viewModel.estadoFormulario.value.cargando)
        assertEquals("thomas@kaiju.cl", usuarioGuardado(idThomas).correo)
    }

    @Test
    fun cambiarSoloElNombreConservandoElMismoCorreoNoEsError() {
        viewModel.cargarUsuario(idThomas)
        viewModel.actualizarNombre("Thomas Cambiado")

        var guardado = false
        viewModel.guardarCambios { guardado = true }

        assertTrue(guardado)
        assertNull(viewModel.estadoFormulario.value.errores.errorEmail)
    }

    @Test
    fun cambiarElRolSeGuarda() {
        viewModel.cargarUsuario(idThomas)
        viewModel.actualizarRol(Rol.ADMINISTRADOR)

        var guardado = false
        viewModel.guardarCambios { guardado = true }

        assertTrue(guardado)
        assertEquals(Rol.ADMINISTRADOR, usuarioGuardado(idThomas).rol)
    }

    @Test
    fun sinCargarUnUsuarioNoSeGuardaNada() {
        var guardado = false
        viewModel.guardarCambios { guardado = true }

        assertFalse(guardado)
    }

    @Test
    fun alEscribirEnUnCampoSeQuitaSoloSuError() {
        viewModel.cargarUsuario(idThomas)
        viewModel.actualizarNombre("")
        viewModel.actualizarEmail("malo")
        viewModel.validarFormulario()

        viewModel.actualizarNombre("Thomas")

        val errores = viewModel.estadoFormulario.value.errores
        assertNull(errores.errorNombre)
        assertNotNull(errores.errorEmail)
    }
}