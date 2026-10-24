package com.example.pockettrackkaiju_tea


import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.repository.UsuarioRepository
import com.example.pockettrackkaiju_tea.viewmodel.RegistroUsuarioViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegistroUsuarioViewModelTest {

    @get:Rule
    val reglaMain = ReglaDispatcherPrincipal()

    private lateinit var usuarioDao: FakeUsuarioDao
    private lateinit var viewModel: RegistroUsuarioViewModel

    @Before
    fun preparar() {
        usuarioDao = FakeUsuarioDao()
        // Al crearse, el ViewModel carga los 3 usuarios de prueba (thomas@, andres@ y diego@kaiju.cl).
        viewModel = RegistroUsuarioViewModel(UsuarioRepository(usuarioDao))
    }

    private fun completarFormularioValido(
        nombre: String = "Camila Rojas",
        email: String = "camila@kaiju.cl",
        password: String = "abcd1234",
        confirmar: String = "abcd1234"
    ) {
        viewModel.actualizarNombre(nombre)
        viewModel.actualizarEmail(email)
        viewModel.actualizarPassword(password)
        viewModel.actualizarConfirmarPassword(confirmar)
    }

    @Test
    fun formularioVacioMuestraTodosLosErrores() {
        assertFalse(viewModel.validarFormulario())

        val errores = viewModel.estadoFormulario.value.errores
        assertNotNull(errores.errorNombre)
        assertNotNull(errores.errorEmail)
        assertNotNull(errores.errorPassword)
        assertNotNull(errores.errorConfirmarPassword)
    }

    @Test
    fun nombreConMenosDe3CaracteresEsInvalido() {
        completarFormularioValido(nombre = "Al")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorNombre)
    }

    @Test
    fun correoSinFormatoEsInvalido() {
        completarFormularioValido(email = "camila.kaiju.cl")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorEmail)
    }

    @Test
    fun noSePuedeRegistrarUnCorreoYaExistente() {
        completarFormularioValido(email = "thomas@kaiju.cl")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorEmail)
    }

    @Test
    fun elCorreoRepetidoSeDetectaSinImportarMayusculas() {
        completarFormularioValido(email = "THOMAS@Kaiju.cl")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorEmail)
    }

    @Test
    fun passwordConMenosDe4CaracteresEsInvalida() {
        completarFormularioValido(password = "123", confirmar = "123")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorPassword)
    }

    @Test
    fun confirmacionDistintaEsInvalida() {
        completarFormularioValido(confirmar = "otraClave")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorConfirmarPassword)
    }

    @Test
    fun formularioCorrectoNoTieneErrores() {
        completarFormularioValido()

        assertTrue(viewModel.validarFormulario())
        val errores = viewModel.estadoFormulario.value.errores
        assertNull(errores.errorNombre)
        assertNull(errores.errorEmail)
        assertNull(errores.errorPassword)
        assertNull(errores.errorConfirmarPassword)
    }

    @Test
    fun registrarGuardaElUsuarioConCorreoEnMinusculas() {
        completarFormularioValido(email = "  Camila@Kaiju.CL ")

        assertTrue(viewModel.registrarUsuario())

        val nuevo = usuarioDao.lista.last()
        assertEquals("camila@kaiju.cl", nuevo.correo)
        assertEquals("Camila Rojas", nuevo.nombre)
        assertEquals(4, usuarioDao.lista.size)
    }

    @Test
    fun todoUsuarioNuevoQuedaConRolVendedor() {
        // Nadie puede registrarse como administrador desde la pantalla de registro.
        completarFormularioValido()

        viewModel.registrarUsuario()

        assertEquals(Rol.VENDEDOR, usuarioDao.lista.last().rol)
    }

    @Test
    fun registroInvalidoNoGuardaNada() {
        completarFormularioValido(confirmar = "distinta")

        assertFalse(viewModel.registrarUsuario())

        assertEquals(3, usuarioDao.lista.size)
    }

    @Test
    fun alRegistrarElFormularioSeLimpia() {
        completarFormularioValido()

        viewModel.registrarUsuario()

        assertEquals("", viewModel.estadoFormulario.value.nombre)
        assertEquals("", viewModel.estadoFormulario.value.email)
    }
}
