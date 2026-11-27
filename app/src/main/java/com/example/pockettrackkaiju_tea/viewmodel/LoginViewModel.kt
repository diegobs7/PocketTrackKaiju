package com.example.pockettrackkaiju_tea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockettrackkaiju_tea.model.ErroresLogin
import com.example.pockettrackkaiju_tea.model.FormularioLogin
import com.example.pockettrackkaiju_tea.model.Usuario
import com.example.pockettrackkaiju_tea.remote.ErrorBackend
import com.example.pockettrackkaiju_tea.repository.UsuarioRepository
import com.example.pockettrackkaiju_tea.util.ConfigServidor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class LoginViewModel(
    private val repository: UsuarioRepository,
    /** Dirección del backend editable desde el login. Null cuando no hay servidor (pruebas). */
    private val servidor: ConfigServidor? = null
) : ViewModel() {
    private val _estadoFormulario = MutableStateFlow(
        FormularioLogin(urlServidor = servidor?.urlBase() ?: "")
    )
    val estadoFormulario: StateFlow<FormularioLogin> = _estadoFormulario.asStateFlow()

    fun actualizarEmail(valor: String) = _estadoFormulario.update {
        it.copy(email = valor, errores = it.errores.copy(errorEmail = null))
    }
    fun actualizarPassword(valor: String) = _estadoFormulario.update {
        it.copy(password = valor, errores = it.errores.copy(errorPassword = null))
    }
    fun actualizarUrlServidor(valor: String) = _estadoFormulario.update {
        it.copy(urlServidor = valor, errores = it.errores.copy(errorServidor = null))
    }
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
            estado.password.length < 8 -> "La contraseña debe tener minimo 8 caracteres"
            else -> null
        }

        val errorServidor = if (servidor != null && ConfigServidor.normalizar(estado.urlServidor) == null)
            "Ingresa una dirección válida, por ejemplo 54.12.34.56:8080."
        else null

        _estadoFormulario.update {
            it.copy(errores = ErroresLogin(errorEmail, errorPassword, errorServidor))
        }
        return errorEmail == null && errorPassword == null && errorServidor == null
    }

    fun iniciarSesion(onExito: (Usuario, Boolean) -> Unit) {
        if (!validarFormulario()) return
        if (_estadoFormulario.value.cargando) return

        val estado = _estadoFormulario.value
        // Se guarda antes de llamar: la petición de login ya debe ir a la dirección nueva.
        servidor?.guardar(estado.urlServidor)
        _estadoFormulario.update { it.copy(cargando = true) }
        viewModelScope.launch {
            try {
                val usuario = repository.iniciarSesion(estado.email, estado.password)
                _estadoFormulario.value = FormularioLogin(urlServidor = servidor?.urlBase() ?: "")
                onExito(usuario, estado.recordarme)
            } catch (e: ErrorBackend) {
                val mensaje = when {
                    e.esCredencialesInvalidas -> "Correo o contraseña incorrectos."
                    e.esSinConexion -> "No se pudo conectar con el servidor."
                    else -> e.message ?: "No se pudo iniciar sesión."
                }
                _estadoFormulario.update {
                    it.copy(cargando = false, errores = ErroresLogin(null, mensaje))
                }
            }
        }
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}