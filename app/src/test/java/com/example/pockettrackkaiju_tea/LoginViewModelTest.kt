package com.example.pockettrackkaiju_tea

import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.model.Usuario
import com.example.pockettrackkaiju_tea.repository.UsuarioRepository
import com.example.pockettrackkaiju_tea.viewmodel.LoginViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val reglaMain = ReglaDispatcherPrincipal()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun preparar() {
        // Al crearse, el ViewModel carga los 3 usuarios de prueba si la tabla está vacía
        // (andres@kaiju.cl es ADMINISTRADOR, clave 1234).
        viewModel = LoginViewModel(UsuarioRepository(FakeUsuarioDao()))
    }

    private fun completarFormulario(email: String, password: String) {
        viewModel.actualizarEmail(email)
        viewModel.actualizarPassword(password)
    }

    // ---------- Validaciones del formulario ----------

    @Test
    fun correoVacioEsInvalido() {
        completarFormulario("", "1234")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorEmail)
    }

    @Test
    fun correoSinFormatoEsInvalido() {
        completarFormulario("esto-no-es-un-correo", "1234")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorEmail)
    }

    @Test
    fun passwordVaciaEsInvalida() {
        completarFormulario("andres@kaiju.cl", "")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorPassword)
    }

    @Test
    fun passwordConMenosDe4CaracteresEsInvalida() {
        completarFormulario("andres@kaiju.cl", "123")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorPassword)
    }

    @Test
    fun formularioCorrectoNoTieneErrores() {
        completarFormulario("andres@kaiju.cl", "1234")

        assertTrue(viewModel.validarFormulario())
        assertNull(viewModel.estadoFormulario.value.errores.errorEmail)
        assertNull(viewModel.estadoFormulario.value.errores.errorPassword)
    }

    // ---------- Inicio de sesión ----------

    @Test
    fun credencialesCorrectasEntreganElUsuarioYElRecordarme() {
        var usuarioRecibido: Usuario? = null
        var recordarRecibido = false
        completarFormulario("andres@kaiju.cl", "1234")
        viewModel.actualizarRecordarme(true)

        viewModel.iniciarSesion { usuario, recordar ->
            usuarioRecibido = usuario
            recordarRecibido = recordar
        }

        assertEquals(Rol.ADMINISTRADOR, usuarioRecibido?.rol)
        assertTrue(recordarRecibido)
    }

    @Test
    fun elCorreoSeNormalizaConEspaciosYMayusculas() {
        var usuarioRecibido: Usuario? = null
        completarFormulario("  ANDRES@Kaiju.CL ", "1234")

        viewModel.iniciarSesion { usuario, _ -> usuarioRecibido = usuario }

        assertEquals("andres@kaiju.cl", usuarioRecibido?.correo)
    }

    @Test
    fun alIngresarBienElFormularioSeLimpia() {
        completarFormulario("andres@kaiju.cl", "1234")

        viewModel.iniciarSesion { _, _ -> }

        assertEquals("", viewModel.estadoFormulario.value.email)
        assertEquals("", viewModel.estadoFormulario.value.password)
    }

    @Test
    fun passwordIncorrectaNoDejaEntrarYMuestraError() {
        var entro = false
        completarFormulario("andres@kaiju.cl", "9999")

        viewModel.iniciarSesion { _, _ -> entro = true }

        assertFalse(entro)
        assertNotNull(viewModel.estadoFormulario.value.errores.errorPassword)
    }

    @Test
    fun correoNoRegistradoNoDejaEntrar() {
        var entro = false
        completarFormulario("fantasma@kaiju.cl", "1234")

        viewModel.iniciarSesion { _, _ -> entro = true }

        assertFalse(entro)
    }

    @Test
    fun conFormularioInvalidoNoSeIntentaIniciarSesion() {
        var entro = false
        completarFormulario("", "")

        viewModel.iniciarSesion { _, _ -> entro = true }

        assertFalse(entro)
        assertNotNull(viewModel.estadoFormulario.value.errores.errorEmail)
    }
}