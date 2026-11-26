package com.example.pockettrackkaiju_tea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockettrackkaiju_tea.model.Usuario
import com.example.pockettrackkaiju_tea.remote.ErrorBackend
import com.example.pockettrackkaiju_tea.repository.UsuarioRepository
import com.example.pockettrackkaiju_tea.util.SesionGuardada
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repository: UsuarioRepository,
    private val sesionGuardada: SesionGuardada
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    init {
        viewModelScope.launch {
            if (sesionGuardada.obtenerUsuarioId() != null) {
                try {
                    _usuarioActual.value = repository.obtenerPerfil()
                } catch (e: ErrorBackend) {
                    sesionGuardada.borrar()
                }
            }
            _cargando.value = false
        }
    }

    fun iniciarSesion(usuario: Usuario, recordar: Boolean) {
        _usuarioActual.value = usuario
        if (recordar) sesionGuardada.guardar(usuario.id) else sesionGuardada.borrar()
    }

    fun cerrarSesion() {
        repository.cerrarSesion()
        _usuarioActual.value = null
        sesionGuardada.borrar()
    }
}