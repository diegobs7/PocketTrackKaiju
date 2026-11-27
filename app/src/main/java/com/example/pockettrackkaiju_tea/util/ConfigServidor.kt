package com.example.pockettrackkaiju_tea.util

import android.content.Context
import androidx.core.content.edit

/**
 * Dirección del backend de Kaiju. Se guarda en SharedPreferences para poder cambiarla desde
 * la pantalla de login sin recompilar: la IP de la instancia de AWS cambia cuando se reinicia,
 * y el docente puede apuntar la app a un backend local.
 */
class ConfigServidor(contexto: Context) {

    private val preferencias =
        contexto.getSharedPreferences("config_servidor", Context.MODE_PRIVATE)

    /** URL base vigente, siempre con "http://" o "https://" y barra final. */
    fun urlBase(): String =
        preferencias.getString(CLAVE_URL, null) ?: URL_POR_DEFECTO

    /** Guarda la URL si es válida y devuelve true; si no lo es, no cambia nada. */
    fun guardar(texto: String): Boolean {
        val url = normalizar(texto) ?: return false
        preferencias.edit { putString(CLAVE_URL, url) }
        return true
    }

    companion object {
        /** 10.0.2.2 es el equipo anfitrión visto desde el emulador de Android Studio. */
        const val URL_POR_DEFECTO = "http://10.0.2.2:8080/"
        private const val CLAVE_URL = "url_base"

        /**
         * Acepta "54.1.2.3:8080", "http://54.1.2.3:8080" o "https://mi.dominio/" y devuelve
         * la URL completa con barra final, o null si el texto no sirve como dirección.
         */
        fun normalizar(texto: String): String? {
            val limpio = texto.trim()
            if (limpio.isEmpty() || limpio.any { it.isWhitespace() }) return null
            val conEsquema =
                if (limpio.startsWith("http://") || limpio.startsWith("https://")) limpio
                else "http://$limpio"
            val sinResto = conEsquema.substringAfter("://")
            if (sinResto.isEmpty() || sinResto.startsWith("/") || sinResto.startsWith(":")) return null
            return conEsquema.trimEnd('/') + "/"
        }
    }
}
