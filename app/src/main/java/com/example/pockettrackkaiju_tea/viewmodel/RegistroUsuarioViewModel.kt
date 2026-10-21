package com.example.pockettrackkaiju_tea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockettrackkaiju_tea.model.ErroresRegistroUsuario
import com.example.pockettrackkaiju_tea.model.FormularioRegistroUsuario
import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.model.Usuario
import com.example.pockettrackkaiju_tea.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistroUsuarioViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _estadoFormulario = MutableStateFlow(FormularioRegistroUsuario())
    val estadoFormulario: StateFlow<FormularioRegistroUsuario> = _estadoFormulario.asStateFlow()

    private val usuarios: StateFlow<List<Usuario>> = repository.usuarios
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        viewModelScope.launch { repository.cargarUsuariosDePrueba() }
    }

    fun actualizarNombre(valor: String) = _estadoFormulario.update { it.copy(nombre = valor) }
    fun actualizarEmail(valor: String) = _estadoFormulario.update { it.copy(email = valor) }
    fun actualizarPassword(valor: String) = _estadoFormulario.update { it.copy(password = valor) }
    fun actualizarConfirmarPassword(valor: String) = _estadoFormulario.update { it.copy(confirmarPassword = valor) }

    fun validarFormulario(): Boolean {
        val estado = _estadoFormulario.value
        val correoNormalizado = estado.email.trim().lowercase()

        val errorNombre = when {
            estado.nombre.isBlank() -> "El nombre es obligatorio."
            estado.nombre.length < 3 -> "El nombre debe tener un minimo de 3 caracteres."
            else -> null
        }

        val errorEmail = when {
            estado.email.isBlank() -> "El correo electronico es obligatorio."
            !EMAIL_REGEX.matches(estado.email.trim()) -> "Ingrese un correo valido."
            usuarios.value.any { it.correo == correoNormalizado } -> "Ya existe una cuenta con ese correo."
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

        val estado = _estadoFormulario.value
        val usuario = Usuario(
            nombre = estado.nombre.trim(),
            correo = estado.email.trim().lowercase(),
            contra = estado.password,
            rol = Rol.VENDEDOR
        )
        viewModelScope.launch { repository.guardar(usuario) }

        _estadoFormulario.value = FormularioRegistroUsuario()
        return true
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}