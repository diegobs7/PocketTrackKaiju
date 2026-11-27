package com.example.pockettrackkaiju_tea.repository

import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.model.Usuario
import com.example.pockettrackkaiju_tea.remote.ErrorBackend
import com.example.pockettrackkaiju_tea.remote.kaiju.ActualizarUsuarioRequest
import com.example.pockettrackkaiju_tea.remote.kaiju.CambiarRolRequest
import com.example.pockettrackkaiju_tea.remote.kaiju.CrearUsuarioRequest
import com.example.pockettrackkaiju_tea.remote.kaiju.ErrorResponse
import com.example.pockettrackkaiju_tea.remote.kaiju.KaijuApi
import com.example.pockettrackkaiju_tea.remote.kaiju.LoginRequest
import com.example.pockettrackkaiju_tea.remote.kaiju.RegistroRequest
import com.example.pockettrackkaiju_tea.remote.kaiju.UsuarioResponse
import com.example.pockettrackkaiju_tea.util.SesionGuardada
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

/**
 * Implementación de [UsuarioRepository] contra el backend Spring Boot.
 * Traduce toda falla a [ErrorBackend], que es lo único que los ViewModels saben manejar.
 *
 * El token vive en memoria y también en [SesionGuardada], para que "Recordarme" pueda
 * restaurar la sesión al abrir la app (SesionViewModel luego llama a [obtenerPerfil]).
 */
class UsuarioRepositoryRemoto(
    private val api: KaijuApi,
    private val sesion: SesionGuardada
) : UsuarioRepository {

    @Volatile
    private var token: String? = null

    /** Token vigente: el de memoria o, tras reabrir la app, el guardado en la sesión. */
    fun tokenActual(): String? = token ?: sesion.obtenerToken()?.also { token = it }

    override suspend fun registrar(nombre: String, correo: String, password: String) {
        llamar {
            api.registrar(RegistroRequest(nombre.trim(), correo.trim().lowercase(), password))
        }
    }

    override suspend fun iniciarSesion(correo: String, password: String): Usuario {
        val respuesta = llamar {
            api.login(LoginRequest(correo.trim().lowercase(), password))
        }
        token = respuesta.accessToken
        sesion.guardarToken(respuesta.accessToken)
        return try {
            llamar { api.perfil() }.aUsuario()
        } catch (e: ErrorBackend) {
            cerrarSesion()
            throw e
        }
    }

    override suspend fun obtenerPerfil(): Usuario = llamar { api.perfil() }.aUsuario()

    override fun cerrarSesion() {
        token = null
        sesion.borrar()
    }

    override suspend fun listar(): List<Usuario> =
        llamar { api.listar() }.filter { it.activo }.map { it.aUsuario() }

    override suspend fun crear(nombre: String, correo: String, password: String, rol: Rol): Usuario =
        llamar {
            api.crear(CrearUsuarioRequest(nombre.trim(), correo.trim().lowercase(), password, rol.name))
        }.aUsuario()

    override suspend fun editar(id: Int, nombre: String, correo: String): Usuario = llamar {
        // El PUT reemplaza el perfil completo: se lee el actual para no borrar RUT, teléfono, etc.
        val actual = api.obtener(id.toLong())
        api.actualizar(
            id.toLong(),
            ActualizarUsuarioRequest(
                nombre = nombre.trim(),
                email = correo.trim().lowercase(),
                rut = actual.rut,
                apellidos = actual.apellidos,
                telefono = actual.telefono,
                fechaNacimiento = actual.fechaNacimiento,
                region = actual.region,
                direccion = actual.direccion
            )
        )
    }.aUsuario()

    override suspend fun cambiarRol(id: Int, rol: Rol): Usuario =
        llamar { api.cambiarRol(id.toLong(), CambiarRolRequest(rol.name)) }.aUsuario()

    override suspend fun eliminar(id: Int) {
        llamar { api.eliminar(id.toLong()) }
    }

    private suspend fun <T> llamar(bloque: suspend () -> T): T =
        try {
            bloque()
        } catch (e: HttpException) {
            throw ErrorBackend(e.code(), mensajeDe(e))
        } catch (e: IOException) {
            throw ErrorBackend(null, "No se pudo conectar con el servidor.")
        }

    private fun mensajeDe(e: HttpException): String {
        val delServidor = try {
            Gson().fromJson(e.response()?.errorBody()?.string(), ErrorResponse::class.java)?.message
        } catch (ignorada: Exception) {
            null
        }
        return delServidor ?: when (e.code()) {
            401 -> "Correo o contraseña incorrectos."
            403 -> "No tienes permiso para hacer esto."
            404 -> "Usuario no encontrado."
            409 -> "Ya existe una cuenta con ese correo."
            else -> "El servidor respondió con un error (${e.code()})."
        }
    }

    private fun UsuarioResponse.aUsuario() = Usuario(
        id = id.toInt(),
        nombre = nombre,
        correo = email,
        rol = Rol.valueOf(rol)
    )
}
