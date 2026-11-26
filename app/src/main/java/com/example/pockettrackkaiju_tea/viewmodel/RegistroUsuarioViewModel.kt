package com.example.pockettrackkaiju_tea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockettrackkaiju_tea.model.ErroresRegistroUsuario
import com.example.pockettrackkaiju_tea.model.FormularioRegistroUsuario
import com.example.pockettrackkaiju_tea.remote.ErrorBackend
import com.example.pockettrackkaiju_tea.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistroUsuarioViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _estadoFormulario = MutableStateFlow(FormularioRegistroUsuario())
    val estadoFormulario: StateFlow<FormularioRegistroUsuario> = _estadoFormulario.asStateFlow()

    fun actualizarNombre(valor: String) = _estadoFormulario.update {
        it.copy(nombre = valor, errores = it.errores.copy(errorNombre = null))
    }
    fun actualizarEmail(valor: String) = _estadoFormulario.update {
        it.copy(email = valor, errores = it.errores.copy(errorEmail = null))
    }
    fun actualizarPassword(valor: String) = _estadoFormulario.update {
        it.copy(
            password = valor,
            errores = it.errores.copy(errorPassword = null, errorConfirmarPassword = null)
        )
    }
    fun actualizarConfirmarPassword(valor: String) = _estadoFormulario.update {
        it.copy(confirmarPassword = valor, errores = it.errores.copy(errorConfirmarPassword = null))
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

        val errorPassword = when {
            estado.password.isBlank() -> "La contraseña es obligatoria."
            estado.password.length < 8 -> "La contraseña debe tener minimo 8 caracteres"
            estado.password.length > 72 -> "La contraseña puede tener como maximo 72 caracteres"
            else -> null
        }
        val errorConfirmarPassword = when {
            estado.confirmarPassword.isBlank() -> "Confirma tu contraseña."
            estado.confirmarPassword != estado.password -> "Las contraseñas no coinciden."
            else -> null
        }

        _estadoFormulario.update {
            it.copy(
                errores = ErroresRegistroUsuario(
                    errorNombre,
                    errorEmail,
                    errorPassword,
                    errorConfirmarPassword,
                )
            )
        }
        return errorNombre == null && errorEmail == null && errorPassword == null && errorConfirmarPassword == null
    }

    fun registrarUsuario(onExito: () -> Unit) {
        if (_estadoFormulario.value.cargando) return
        if (!validarFormulario()) return

        val estado = _estadoFormulario.value
        _estadoFormulario.update { it.copy(cargando = true) }
        viewModelScope.launch {
            try {
                repository.registrar(estado.nombre.trim(), estado.email.trim().lowercase(), estado.password)
                _estadoFormulario.value = FormularioRegistroUsuario()
                onExito()
            } catch (e: ErrorBackend) {
                _estadoFormulario.update {
                    it.copy(cargando = false, errores = erroresDelBackend(e, it.errores))
                }
            }
        }
    }

    private fun erroresDelBackend(
        e: ErrorBackend,
        actuales: ErroresRegistroUsuario
    ): ErroresRegistroUsuario = when {
        e.esCorreoRepetido -> actuales.copy(errorEmail = "Ya existe una cuenta con ese correo.")
        e.esSinConexion -> actuales.copy(errorConfirmarPassword = "No se pudo conectar con el servidor.")
        else -> actuales.copy(errorConfirmarPassword = e.message ?: "No se pudo registrar la cuenta.")
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}