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

    /** Valida y guarda la dirección escrita en el engranaje. Devuelve true si quedó guardada. */
    fun guardarServidor(): Boolean {
        val config = servidor ?: return false
        val texto = _estadoFormulario.value.urlServidor
        if (!config.guardar(texto)) {
            _estadoFormulario.update {
                it.copy(
                    errores = it.errores.copy(
                        errorServidor = "Ingresa una dirección válida, por ejemplo 54.12.34.56:8080."
                    )
                )
            }
            return false
        }
        _estadoFormulario.update {
            it.copy(urlServidor = config.urlBase(), errores = it.errores.copy(errorServidor = null))
        }
        return true
    }

    /** Vuelve a la dirección por defecto y la deja visible en el campo. */
    fun restablecerServidor() {
        val config = servidor ?: return
        config.restablecer()
        _estadoFormulario.update {
            it.copy(urlServidor = config.urlBase(), errores = it.errores.copy(errorServidor = null))
        }
    }

    /** Descarta lo escrito sin guardar y muestra de nuevo la dirección vigente. */
    fun cancelarEdicionServidor() {
        val config = servidor ?: return
        _estadoFormulario.update {
            it.copy(urlServidor = config.urlBase(), errores = it.errores.copy(errorServidor = null))
        }
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

        _estadoFormulario.update {
            it.copy(errores = ErroresLogin(errorEmail, errorPassword))
        }
        return errorEmail == null && errorPassword == null
    }

    fun iniciarSesion(onExito: (Usuario, Boolean) -> Unit) {
        if (!validarFormulario()) return
        if (_estadoFormulario.value.cargando) return

        val estado = _estadoFormulario.value
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