package com.example.pockettrackkaiju_tea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockettrackkaiju_tea.model.ErroresLogin
import com.example.pockettrackkaiju_tea.model.FormularioLogin
import com.example.pockettrackkaiju_tea.model.Usuario
import com.example.pockettrackkaiju_tea.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {
    private val _estadoFormulario = MutableStateFlow(FormularioLogin())
    val estadoFormulario: StateFlow<FormularioLogin> = _estadoFormulario.asStateFlow()

    init {

        viewModelScope.launch { repository.cargarUsuariosDePrueba() }
    }

    fun actualizarEmail(valor: String) = _estadoFormulario.update { it.copy(email = valor) }
    fun actualizarPassword(valor: String) = _estadoFormulario.update { it.copy(password = valor) }
    fun actualizarRecordarme(valor: Boolean) = _estadoFormulario.update { it.copy(recordarme = valor) }

    fun validarFormulario(): Boolean {
        val estado = _estadoFormulario.value

        val errorEmail = when {
            estado.email.isBlank() -> "El correo es obligatorio."
            !EMAIL_REGEX.matches(estado.email.trim()) -> "Ingresa un correo valido."
            else -> null
        }

        val errorPassword = when {
            estado.password.isBlank() -> "La contraseña es obligatoria."
            estado.password.length < 4 -> "La contraseña debe tener minimo 4 caracteres"
            else -> null
        }

        _estadoFormulario.update {
            it.copy(errores = ErroresLogin(errorEmail, errorPassword))
        }
        return errorEmail == null && errorPassword == null
    }

    fun iniciarSesion(onExito: (Usuario, Boolean) -> Unit) {
        if (!validarFormulario()) return

        val estado = _estadoFormulario.value
        viewModelScope.launch {
            val usuario = repository.autenticar(estado.email, estado.password)
            if (usuario != null) {
                _estadoFormulario.value = FormularioLogin()
                onExito(usuario, estado.recordarme)
            } else {
                _estadoFormulario.update {
                    it.copy(errores = ErroresLogin(null, "Correo o contraseña incorrectos."))
                }
            }
        }
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}