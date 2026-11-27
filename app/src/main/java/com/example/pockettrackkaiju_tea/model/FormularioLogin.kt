package com.example.pockettrackkaiju_tea.model

data class FormularioLogin(
    val email: String = "",
    val password: String = "",
    val recordarme: Boolean = false,
    val urlServidor: String = "",
    val cargando: Boolean = false,
    val errores: ErroresLogin = ErroresLogin()
)