package com.example.pockettrackkaiju_tea.model

data class ErroresRegistroUsuario (
    val errorNombre: String? = null,
    val errorEmail: String? = null,
    val errorPassword: String? = null,
    val errorConfirmarPassword: String? = null,
)