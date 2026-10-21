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

    fun borrar() {
        preferencias.edit { remove(CLAVE_USUARIO_ID) }
    }

    private companion object {
        const val CLAVE_USUARIO_ID = "usuario_id"
    }
}