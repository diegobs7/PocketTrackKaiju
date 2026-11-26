package com.example.pockettrackkaiju_tea.remote

/**
 * Error que el repositorio de usuarios entrega a los ViewModels cuando el backend
 * @param codigo código HTTP de la respuesta (401, 403, 409...) o null si no hubo
 * respuesta (sin internet, servidor apagado, tiempo agotado).
 */
class ErrorBackend(
    val codigo: Int?,
    mensaje: String
) : Exception(mensaje) {

    val esSinConexion: Boolean get() = codigo == null
    val esCredencialesInvalidas: Boolean get() = codigo == 401
    val esSinPermiso: Boolean get() = codigo == 403
    val esCorreoRepetido: Boolean get() = codigo == 409
}