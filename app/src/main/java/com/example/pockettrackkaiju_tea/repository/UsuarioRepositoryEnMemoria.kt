package com.example.pockettrackkaiju_tea.repository

import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.model.Usuario
import com.example.pockettrackkaiju_tea.remote.ErrorBackend

class UsuarioRepositoryEnMemoria : UsuarioRepository {

    private class Cuenta(var usuario: Usuario, val password: String)

    private val cuentas = mutableListOf<Cuenta>()
    private var siguienteId = 1
    private var idSesion: Int? = null

    init {
        agregar("Thomas Araya", "thomas@kaiju.cl", "12345678", Rol.VENDEDOR)
        agregar("Andrés Marín", "andres@kaiju.cl", "12345678", Rol.ADMINISTRADOR)
        agregar("Diego Bergeret", "diego@kaiju.cl", "12345678", Rol.ENCARGADO_INVENTARIO)
    }

    private fun agregar(nombre: String, correo: String, password: String, rol: Rol): Usuario {
        val correoNormalizado = correo.trim().lowercase()
        if (cuentas.any { it.usuario.correo == correoNormalizado }) {
            throw ErrorBackend(409, "Ya existe una cuenta con ese correo.")
        }
        val usuario = Usuario(siguienteId++, nombre.trim(), correoNormalizado, rol)
        cuentas.add(Cuenta(usuario, password))
        return usuario
    }

    private fun buscar(id: Int): Cuenta =
        cuentas.find { it.usuario.id == id } ?: throw ErrorBackend(404, "Usuario no encontrado.")

    override suspend fun registrar(nombre: String, correo: String, password: String) {
        agregar(nombre, correo, password, Rol.VENDEDOR)
    }

    override suspend fun iniciarSesion(correo: String, password: String): Usuario {
        val cuenta = cuentas.find {
            it.usuario.correo == correo.trim().lowercase() && it.password == password
        } ?: throw ErrorBackend(401, "Correo o contraseña incorrectos.")
        idSesion = cuenta.usuario.id
        return cuenta.usuario
    }

    override suspend fun obtenerPerfil(): Usuario {
        val id = idSesion ?: throw ErrorBackend(401, "No hay una sesión activa.")
        return buscar(id).usuario
    }

    override fun cerrarSesion() {
        idSesion = null
    }

    override suspend fun listar(): List<Usuario> = cuentas.map { it.usuario }

    override suspend fun crear(nombre: String, correo: String, password: String, rol: Rol): Usuario =
        agregar(nombre, correo, password, rol)

    override suspend fun editar(id: Int, nombre: String, correo: String): Usuario {
        val cuenta = buscar(id)
        val correoNormalizado = correo.trim().lowercase()
        if (cuentas.any { it.usuario.id != id && it.usuario.correo == correoNormalizado }) {
            throw ErrorBackend(409, "Ya existe una cuenta con ese correo.")
        }
        cuenta.usuario = cuenta.usuario.copy(nombre = nombre.trim(), correo = correoNormalizado)
        return cuenta.usuario
    }

    override suspend fun cambiarRol(id: Int, rol: Rol): Usuario {
        val cuenta = buscar(id)
        cuenta.usuario = cuenta.usuario.copy(rol = rol)
        return cuenta.usuario
    }

    override suspend fun eliminar(id: Int) {
        cuentas.remove(buscar(id))
        if (idSesion == id) idSesion = null
    }
}