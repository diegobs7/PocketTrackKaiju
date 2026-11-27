package com.example.pockettrackkaiju_tea.remote.kaiju

/** Clases que reflejan las peticiones y respuestas del backend (/api/v1). */

const val APLICACION_KAIJU = "KAIJU"

data class LoginRequest(
    val email: String,
    val password: String,
    val aplicacion: String = APLICACION_KAIJU
)

data class TokenResponse(
    val accessToken: String,
    val tokenType: String? = null,
    val expiresIn: Long = 0
)

data class RegistroRequest(
    val nombre: String,
    val email: String,
    val password: String,
    val aplicacion: String = APLICACION_KAIJU
)

data class CrearUsuarioRequest(
    val nombre: String,
    val email: String,
    val password: String,
    val rol: String
)

/** El PUT reemplaza todos los datos del perfil: los campos que no se editan se reenvían tal cual. */
data class ActualizarUsuarioRequest(
    val nombre: String,
    val email: String,
    val rut: String? = null,
    val apellidos: String? = null,
    val telefono: String? = null,
    val fechaNacimiento: String? = null,
    val region: String? = null,
    val direccion: String? = null
)

data class CambiarRolRequest(val rol: String)

data class UsuarioResponse(
    val id: Long,
    val nombre: String,
    val email: String,
    val rol: String,
    val aplicacion: String? = null,
    val activo: Boolean = true,
    val rut: String? = null,
    val apellidos: String? = null,
    val telefono: String? = null,
    val fechaNacimiento: String? = null,
    val region: String? = null,
    val direccion: String? = null
)

/** Cuerpo de error del backend: {"status":409,"error":"Conflict","message":"..."} */
data class ErrorResponse(val status: Int? = null, val message: String? = null)
