package com.example.pockettrackkaiju_tea.repository

import com.example.pockettrackkaiju_tea.database.dao.UsuarioDao
import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.model.Usuario
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(private val dao: UsuarioDao) {

    val usuarios: Flow<List<Usuario>> = dao.obtenerTodos()

    suspend fun guardar(usuario: Usuario) = dao.insertar(usuario)

    suspend fun eliminar(usuario: Usuario) = dao.eliminar(usuario)

    suspend fun cargarUsuariosDePrueba() {
        if (dao.contar() > 0) return
        dao.insertar(Usuario(nombre = "Thomas Araya", rol = Rol.VENDEDOR))
        dao.insertar(Usuario(nombre = "Andres Marin", rol = Rol.ADMINISTRADOR))
        dao.insertar(Usuario(nombre = "Diego Bergeret", rol = Rol.ENCARGADO_INVENTARIO))
    }
}