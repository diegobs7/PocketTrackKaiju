package com.example.pockettrackkaiju_tea.repository

import com.example.pockettrackkaiju_tea.database.dao.UsuarioDao
import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.model.Usuario
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(private val dao: UsuarioDao) {

    val usuarios: Flow<List<Usuario>> = dao.obtenerTodos()

    suspend fun guardar(usuario: Usuario) = dao.insertar(usuario)

    suspend fun eliminar(usuario: Usuario) = dao.eliminar(usuario)

    suspend fun existeCorreo(correo: String): Boolean =
        dao.obtenerPorCorreo(correo.trim().lowercase()) != null

    suspend fun autenticar(correo: String, contra: String): Usuario? =
        dao.autenticar(correo.trim().lowercase(), contra)

    suspend fun obtenerPorId(id: Int): Usuario? = dao.obtenerPorId(id)

    suspend fun cargarUsuariosDePrueba() {
        if (dao.contar() > 0) return
        dao.insertar(Usuario(nombre = "Thomas Araya", correo = "thomas@kaiju.cl", contra = "1234", rol = Rol.VENDEDOR))
        dao.insertar(Usuario(nombre = "Andrés Marín", correo = "andres@kaiju.cl", contra = "1234", rol = Rol.ADMINISTRADOR))
        dao.insertar(Usuario(nombre = "Diego Bergeret", correo = "diego@kaiju.cl", contra = "1234", rol = Rol.ENCARGADO_INVENTARIO))
    }
}