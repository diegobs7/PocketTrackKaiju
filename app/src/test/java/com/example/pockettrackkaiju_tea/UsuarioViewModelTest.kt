package com.example.pockettrackkaiju_tea


import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.model.TipoMovimiento
import com.example.pockettrackkaiju_tea.repository.MovimientoRepository
import com.example.pockettrackkaiju_tea.repository.UsuarioRepositoryEnMemoria
import com.example.pockettrackkaiju_tea.viewmodel.UsuariosViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UsuariosViewModelTest {

    @get:Rule
    val reglaMain = ReglaDispatcherPrincipal()

    private lateinit var movimientoDao: FakeMovimientoDao
    private lateinit var viewModel: UsuariosViewModel

    @Before
    fun preparar() {
        movimientoDao = FakeMovimientoDao()
        viewModel = UsuariosViewModel(
            UsuarioRepositoryEnMemoria(),
            MovimientoRepository(movimientoDao)
        )
    }

    private fun usuarioPorCorreo(correo: String) =
        viewModel.usuarios.value.first { it.correo == correo }

    private fun registrarMovimientoDe(responsableId: Int) {
        runBlocking {
            movimientoDao.insertar(
                movimientoDePrueba("KAI-001", TipoMovimiento.ENTRADA, 5)
                    .copy(responsableId = responsableId)
            )
        }
    }

    @Test
    fun alCrearseCargaLosUsuarios() {
        assertEquals(3, viewModel.usuarios.value.size)
    }

    @Test
    fun cambiarRolActualizaLaLista() {
        val thomas = usuarioPorCorreo("thomas@kaiju.cl")

        viewModel.cambiarRol(thomas, Rol.ADMINISTRADOR)

        assertEquals(Rol.ADMINISTRADOR, usuarioPorCorreo("thomas@kaiju.cl").rol)
    }

    @Test
    fun eliminarQuitaAlUsuarioDeLaLista() {
        val thomas = usuarioPorCorreo("thomas@kaiju.cl")

        viewModel.eliminarUsuario(thomas)

        assertEquals(2, viewModel.usuarios.value.size)
        assertTrue(viewModel.usuarios.value.none { it.correo == "thomas@kaiju.cl" })
    }

    @Test
    fun noSeEliminaAUnUsuarioConMovimientos() {
        val diego = usuarioPorCorreo("diego@kaiju.cl")
        registrarMovimientoDe(diego.id)

        assertTrue(viewModel.tieneMovimientos(diego))
        viewModel.eliminarUsuario(diego)

        assertEquals(3, viewModel.usuarios.value.size)
    }

    @Test
    fun siElBackendFallaSeGuardaUnMensajeDeError() {
        val thomas = usuarioPorCorreo("thomas@kaiju.cl")
        viewModel.eliminarUsuario(thomas)

        // Eliminarlo otra vez: el backend responde "no encontrado".
        viewModel.eliminarUsuario(thomas)

        assertNotNull(viewModel.error.value)
        viewModel.limpiarError()
        assertNull(viewModel.error.value)
    }
}