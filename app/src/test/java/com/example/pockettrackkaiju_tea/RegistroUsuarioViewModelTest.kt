package com.example.pockettrackkaiju_tea

import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.repository.UsuarioRepositoryEnMemoria
import com.example.pockettrackkaiju_tea.viewmodel.RegistroUsuarioViewModel
import kotlinx.coroutines.runBlocking
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

    private lateinit var repositorio: UsuarioRepositoryEnMemoria
    private lateinit var viewModel: RegistroUsuarioViewModel

    @Before
    fun preparar() {
        // El repositorio en memoria trae 3 usuarios (thomas@, andres@ y diego@kaiju.cl).
        repositorio = UsuarioRepositoryEnMemoria()
        viewModel = RegistroUsuarioViewModel(repositorio)
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

        var registrado = false
        viewModel.registrarUsuario { registrado = true }

        assertFalse(registrado)
        assertNotNull(viewModel.estadoFormulario.value.errores.errorEmail)
        assertFalse(viewModel.estadoFormulario.value.cargando)
    }

    @Test
    fun elCorreoRepetidoSeDetectaSinImportarMayusculas() {
        completarFormularioValido(email = "THOMAS@Kaiju.cl")

        var registrado = false
        viewModel.registrarUsuario { registrado = true }

        assertFalse(registrado)
        assertNotNull(viewModel.estadoFormulario.value.errores.errorEmail)
    }

    @Test
    fun passwordConMenosDe8CaracteresEsInvalida() {
        completarFormularioValido(password = "1234567", confirmar = "1234567")

        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.estadoFormulario.value.errores.errorPassword)
    }

    @Test
    fun passwordConMasDe72CaracteresEsInvalida() {
        val larga = "a".repeat(73)
        completarFormularioValido(password = larga, confirmar = larga)

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

        var registrado = false
        viewModel.registrarUsuario { registrado = true }

        assertTrue(registrado)
        val usuarios = runBlocking { repositorio.listar() }
        val nuevo = usuarios.last()
        assertEquals("camila@kaiju.cl", nuevo.correo)
        assertEquals("Camila Rojas", nuevo.nombre)
        assertEquals(4, usuarios.size)
    }

    @Test
    fun todoUsuarioNuevoQuedaConRolVendedor() {
        // Nadie puede registrarse como administrador desde la pantalla de registro.
        completarFormularioValido()

        viewModel.registrarUsuario { }

        assertEquals(Rol.VENDEDOR, runBlocking { repositorio.listar() }.last().rol)
    }

    @Test
    fun registroInvalidoNoGuardaNada() {
        completarFormularioValido(confirmar = "distinta")

        var registrado = false
        viewModel.registrarUsuario { registrado = true }

        assertFalse(registrado)
        assertEquals(3, runBlocking { repositorio.listar() }.size)
    }

    @Test
    fun alRegistrarElFormularioSeLimpia() {
        completarFormularioValido()

        viewModel.registrarUsuario { }

        assertEquals("", viewModel.estadoFormulario.value.nombre)
        assertEquals("", viewModel.estadoFormulario.value.email)
    }
    @Test
    fun crearConRolGuardaElRolElegido() {
        completarFormularioValido()
        viewModel.actualizarRol(Rol.ENCARGADO_INVENTARIO)

        var creado = false
        viewModel.registrarUsuario(conRol = true) { creado = true }

        assertTrue(creado)
        val nuevo = runBlocking { repositorio.listar() }.last()
        assertEquals("camila@kaiju.cl", nuevo.correo)
        assertEquals(Rol.ENCARGADO_INVENTARIO, nuevo.rol)
    }

    @Test
    fun elRegistroPublicoIgnoraElRolYDejaVendedor() {
        // Sin conRol (registro desde el login), nadie puede elegirse un rol mejor.
        completarFormularioValido()
        viewModel.actualizarRol(Rol.ADMINISTRADOR)

        viewModel.registrarUsuario { }

        assertEquals(Rol.VENDEDOR, runBlocking { repositorio.listar() }.last().rol)
    }

    @Test
    fun crearConRolConCorreoRepetidoMuestraErrorYNoGuarda() {
        completarFormularioValido(email = "thomas@kaiju.cl")
        viewModel.actualizarRol(Rol.ADMINISTRADOR)

        var creado = false
        viewModel.registrarUsuario(conRol = true) { creado = true }

        assertFalse(creado)
        assertNotNull(viewModel.estadoFormulario.value.errores.errorEmail)
        assertEquals(3, runBlocking { repositorio.listar() }.size)
    }
}