package com.example.pockettrackkaiju_tea.util

import android.content.Context
import androidx.core.content.edit

class SesionGuardada(contexto: Context) {

    private val preferencias =
        contexto.getSharedPreferences("sesion", Context.MODE_PRIVATE)

    fun guardar(usuarioId: Int) {
        preferencias.edit { putInt(CLAVE_USUARIO_ID, usuarioId) }
    }

    fun obtenerUsuarioId(): Int? =
        if (preferencias.contains(CLAVE_USUARIO_ID)) {
            preferencias.getInt(CLAVE_USUARIO_ID, 0)
        } else null

    /** Token JWT del backend. Se guarda al iniciar sesión y se borra junto con la sesión. */
    fun guardarToken(token: String) {
        preferencias.edit { putString(CLAVE_TOKEN, token) }
    }

    fun obtenerToken(): String? = preferencias.getString(CLAVE_TOKEN, null)

    fun borrar() {
        preferencias.edit {
            remove(CLAVE_USUARIO_ID)
            remove(CLAVE_TOKEN)
        }
    }

    private companion object {
        const val CLAVE_USUARIO_ID = "usuario_id"
        const val CLAVE_TOKEN = "token"
    }
}
