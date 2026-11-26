package com.example.pockettrackkaiju_tea.repository

import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.model.Usuario

interface UsuarioRepository {

    /** Crea una cuenta nueva. El servidor le asigna el rol VENDEDOR. */
    suspend fun registrar(nombre: String, correo: String, password: String)

    /** Inicia sesión, guarda el token y devuelve el perfil del usuario. */
    suspend fun iniciarSesion(correo: String, password: String): Usuario

    /** Perfil del usuario con la sesión activa, para que recuerde la sesion iniciada. */
    suspend fun obtenerPerfil(): Usuario

    /** Olvida el token de la sesión activa. */
    fun cerrarSesion()

    suspend fun listar(): List<Usuario>

    suspend fun crear(nombre: String, correo: String, password: String, rol: Rol): Usuario

    suspend fun editar(id: Int, nombre: String, correo: String): Usuario

    suspend fun cambiarRol(id: Int, rol: Rol): Usuario

    suspend fun eliminar(id: Int)
}