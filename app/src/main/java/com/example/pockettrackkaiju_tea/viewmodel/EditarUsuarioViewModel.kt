package com.example.pockettrackkaiju_tea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockettrackkaiju_tea.model.ErroresEditarUsuario
import com.example.pockettrackkaiju_tea.model.FormularioEditarUsuario
import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.remote.ErrorBackend
import com.example.pockettrackkaiju_tea.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditarUsuarioViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _estadoFormulario = MutableStateFlow(FormularioEditarUsuario())
    val estadoFormulario: StateFlow<FormularioEditarUsuario> = _estadoFormulario.asStateFlow()

    private var usuarioId: Int? = null
    private var rolOriginal: Rol? = null

    fun cargarUsuario(id: Int) {
        if (usuarioId == id) return
        viewModelScope.launch {
            try {
                val usuario = repository.listar().find { it.id == id }
                if (usuario == null) {
                    mostrarErrorGeneral("No se encontró el usuario.")
                    return@launch
                }
                usuarioId = id
                rolOriginal = usuario.rol
                _estadoFormulario.update {
                    it.copy(nombre = usuario.nombre, email = usuario.correo, rol = usuario.rol)
                }
            } catch (e: ErrorBackend) {
                mostrarErrorGeneral(mensajeGeneral(e))
            }
        }
    }

    fun actualizarNombre(valor: String) = _estadoFormulario.update {
        it.copy(nombre = valor, errores = it.errores.copy(errorNombre = null))
    }
    fun actualizarEmail(valor: String) = _estadoFormulario.update {
        it.copy(email = valor, errores = it.errores.copy(errorEmail = null))
    }
    fun actualizarRol(valor: Rol) = _estadoFormulario.update { it.copy(rol = valor) }

    fun limpiarErrorGeneral() = _estadoFormulario.update {
        it.copy(errores = it.errores.copy(errorGeneral = null))
    }

    fun validarFormulario(): Boolean {
        val estado = _estadoFormulario.value

        val errorNombre = when {
            estado.nombre.isBlank() -> "El nombre es obligatorio."
            estado.nombre.length < 3 -> "El nombre debe tener un minimo de 3 caracteres."
            else -> null
        }

        val errorEmail = when {
            estado.email.isBlank() -> "El correo electronico es obligatorio."
            !EMAIL_REGEX.matches(estado.email.trim()) -> "Ingrese un correo valido."
            else -> null
        }

        _estadoFormulario.update {
            it.copy(errores = it.errores.copy(errorNombre = errorNombre, errorEmail = errorEmail))
        }
        return errorNombre == null && errorEmail == null
    }

    fun guardarCambios(onExito: () -> Unit) {
        val id = usuarioId ?: return
        if (_estadoFormulario.value.cargando) return
        if (!validarFormulario()) return

        val estado = _estadoFormulario.value
        _estadoFormulario.update { it.copy(cargando = true) }
        viewModelScope.launch {
            try {
                repository.editar(id, estado.nombre.trim(), estado.email.trim().lowercase())
                if (estado.rol != rolOriginal) {
                    repository.cambiarRol(id, estado.rol)
                    rolOriginal = estado.rol
                }
                _estadoFormulario.update { it.copy(cargando = false) }
                onExito()
            } catch (e: ErrorBackend) {
                _estadoFormulario.update {
                    it.copy(cargando = false, errores = erroresDelBackend(e, it.errores))
                }
            }
        }
    }

    private fun mostrarErrorGeneral(mensaje: String) = _estadoFormulario.update {
        it.copy(errores = it.errores.copy(errorGeneral = mensaje))
    }

    private fun erroresDelBackend(
        e: ErrorBackend,
        actuales: ErroresEditarUsuario
    ): ErroresEditarUsuario = when {
        e.esCorreoRepetido -> actuales.copy(errorEmail = "Ya existe una cuenta con ese correo.")
        else -> actuales.copy(errorGeneral = mensajeGeneral(e))
    }

    private fun mensajeGeneral(e: ErrorBackend): String = when {
        e.esSinConexion -> "No se pudo conectar con el servidor."
        e.esSinPermiso -> "No tienes permiso para editar usuarios."
        else -> e.message ?: "No se pudo guardar el usuario."
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}