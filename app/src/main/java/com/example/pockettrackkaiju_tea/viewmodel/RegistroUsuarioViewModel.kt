package com.example.pockettrackkaiju_tea.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pockettrackkaiju_tea.model.ErroresRegistroUsuario
import com.example.pockettrackkaiju_tea.model.FormularioRegistroUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegistroUsuarioViewModel : ViewModel() {
    private val _estadoFormulario = MutableStateFlow(FormularioRegistroUsuario())
    val estadoFormulario : StateFlow<FormularioRegistroUsuario> = _estadoFormulario.asStateFlow()

    fun actualizarNombre(valor: String) = _estadoFormulario.update { it.copy(nombre = valor) }
    fun actualizarEmail(valor: String) = _estadoFormulario.update { it.copy(email = valor) }
    fun actualizarPassword(valor: String) = _estadoFormulario.update { it.copy(password = valor) }
    fun actualizarConfirmarPassword(valor: String) = _estadoFormulario.update { it.copy(confirmarPassword = valor) }

    fun validarFormulario() : Boolean {
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
            estado.password.length < 4 -> "La contraseña debe tener minimo 4 caracteres"
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
    fun registrarUsuario(): Boolean {
        if (!validarFormulario()) return false

        _estadoFormulario.value = FormularioRegistroUsuario()
        return true
    }
    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}