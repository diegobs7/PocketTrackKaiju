package com.example.pockettrackkaiju_tea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockettrackkaiju_tea.model.Movimiento
import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.model.Usuario
import com.example.pockettrackkaiju_tea.repository.MovimientoRepository
import com.example.pockettrackkaiju_tea.repository.UsuarioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UsuariosViewModel(
    private val repository: UsuarioRepository,
    movimientoRepository: MovimientoRepository
) : ViewModel() {

    val usuarios: StateFlow<List<Usuario>> = repository.usuarios
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val movimientos: StateFlow<List<Movimiento>> = movimientoRepository.movimientos
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun cambiarRol(usuario: Usuario, nuevoRol: Rol) {
        if (usuario.rol == nuevoRol) return
        viewModelScope.launch { repository.cambiarRol(usuario, nuevoRol) }
    }

    fun tieneMovimientos(usuario: Usuario): Boolean =
        movimientos.value.any { it.responsableId == usuario.id }

    fun eliminarUsuario(usuario: Usuario) {
        if (tieneMovimientos(usuario)) return
        viewModelScope.launch { repository.eliminar(usuario) }
    }
}