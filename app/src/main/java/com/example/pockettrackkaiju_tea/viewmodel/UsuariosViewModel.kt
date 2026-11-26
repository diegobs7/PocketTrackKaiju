package com.example.pockettrackkaiju_tea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockettrackkaiju_tea.model.Movimiento
import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.model.Usuario
import com.example.pockettrackkaiju_tea.remote.ErrorBackend
import com.example.pockettrackkaiju_tea.repository.MovimientoRepository
import com.example.pockettrackkaiju_tea.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UsuariosViewModel(
    private val repository: UsuarioRepository,
    movimientoRepository: MovimientoRepository
) : ViewModel() {

    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _rolFiltro = MutableStateFlow<Rol?>(null)
    val rolFiltro: StateFlow<Rol?> = _rolFiltro.asStateFlow()

    // Lista que se muestra: filtrada por rol y por nombre, sin distinguir mayúsculas.
    val usuariosFiltrados: StateFlow<List<Usuario>> =
        combine(_usuarios, _busqueda, _rolFiltro) { lista, texto, rol ->
            lista.filter { usuario ->
                (rol == null || usuario.rol == rol) &&
                        usuario.nombre.contains(texto.trim(), ignoreCase = true)
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val movimientos: StateFlow<List<Movimiento>> = movimientoRepository.movimientos
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        cargarUsuarios()
    }

    fun cargarUsuarios() {
        viewModelScope.launch {
            ejecutar { _usuarios.value = repository.listar() }
        }
    }

    fun actualizarBusqueda(texto: String) {
        _busqueda.value = texto
    }

    // Tocar el rol que ya está elegido quita el filtro.
    fun seleccionarRol(rol: Rol?) {
        _rolFiltro.value = if (_rolFiltro.value == rol) null else rol
    }

    fun tieneMovimientos(usuario: Usuario): Boolean =
        movimientos.value.any { it.responsableId == usuario.id }

    fun desactivarUsuario(usuario: Usuario) {
        cambiarYRecargar { repository.desactivar(usuario.id) }
    }

    fun reactivarUsuario(usuario: Usuario) {
        cambiarYRecargar { repository.reactivar(usuario.id) }
    }

    fun eliminarUsuario(usuario: Usuario) {
        if (tieneMovimientos(usuario)) return
        cambiarYRecargar { repository.eliminar(usuario.id) }
    }

    fun limpiarError() {
        _error.value = null
    }

    // Ejecuta un cambio en el backend y vuelve a pedir la lista para mostrar el estado real.
    private fun cambiarYRecargar(cambio: suspend () -> Unit) {
        viewModelScope.launch {
            ejecutar {
                cambio()
                _usuarios.value = repository.listar()
            }
        }
    }

    private suspend fun ejecutar(accion: suspend () -> Unit) {
        try {
            accion()
            _error.value = null
        } catch (e: ErrorBackend) {
            _error.value = when {
                e.esSinConexion -> "No se pudo conectar con el servidor."
                e.esSinPermiso -> "No tienes permiso para hacer esto."
                else -> e.message ?: "Ocurrió un error."
            }
        }
    }
}