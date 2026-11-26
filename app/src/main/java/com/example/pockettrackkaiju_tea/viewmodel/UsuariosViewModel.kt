package com.example.pockettrackkaiju_tea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockettrackkaiju_tea.model.Movimiento
import com.example.pockettrackkaiju_tea.model.Usuario
import com.example.pockettrackkaiju_tea.remote.ErrorBackend
import com.example.pockettrackkaiju_tea.repository.MovimientoRepository
import com.example.pockettrackkaiju_tea.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UsuariosViewModel(
    private val repository: UsuarioRepository,
    movimientoRepository: MovimientoRepository
) : ViewModel() {

    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios.asStateFlow()

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

    fun tieneMovimientos(usuario: Usuario): Boolean =
        movimientos.value.any { it.responsableId == usuario.id }

    fun eliminarUsuario(usuario: Usuario) {
        if (tieneMovimientos(usuario)) return
        viewModelScope.launch {
            ejecutar {
                repository.eliminar(usuario.id)
                _usuarios.value = repository.listar()
            }
        }
    }

    fun limpiarError() {
        _error.value = null
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