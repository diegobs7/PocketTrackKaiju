package com.example.pockettrackkaiju_tea.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pockettrackkaiju_tea.model.ErroresLogin
import com.example.pockettrackkaiju_tea.model.FormularioLogin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    private val _estadoFormulario = MutableStateFlow(FormularioLogin())
    val estadoFormulario: StateFlow<FormularioLogin> = _estadoFormulario.asStateFlow()


    fun actualizarEmail(valor: String) = _estadoFormulario.update { it.copy(email = valor) }
    fun actualizarPassword(valor: String) = _estadoFormulario.update { it.copy(password = valor) }
    fun actualizarRecordarme(valor: Boolean) = _estadoFormulario.update { it.copy(recordarme = valor) }

    fun validarFormulario(): Boolean {
        val estado = _estadoFormulario.value

        val errorEmail = when {
            estado.email.isBlank() -> "El correo es obligatorio."
            !EMAIL_REGEX.matches(estado.email.trim()) -> "Correo invalido."
            !tieneDominioPermitido(estado.email) -> "\"Usa un correo @duocuc.cl, @profesor.duoc.cl o @gmail.com\""
            else -> null
        }

        val errorPassword = when {
            estado.password.isBlank() -> "La contrasena es obligatoria."
            estado.password.length < 4 -> "La contrasena debe tener minimo 4 caracteres"
            else -> null
        }

        _estadoFormulario.update {
            it.copy(errores = ErroresLogin(errorEmail, errorPassword))
        }
        return errorEmail == null && errorPassword == null
    }

    fun iniciarSesion(): Boolean {
        if(!validarFormulario()) return false
        return true
    }
    private fun tieneDominioPermitido(email: String): Boolean {
        val dominio = email.trim().substringAfterLast("@").lowercase()
        return dominio in DOMINIOS_PERMITIDOS
    }
    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        val DOMINIOS_PERMITIDOS = listOf("duocuc.cl", "profesor.duoc.cl", "gmail.com")
    }

}