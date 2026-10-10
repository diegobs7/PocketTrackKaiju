package com.example.pockettrackkaiju_tea.model

 data class FormularioRegistroUsuario (
     val nombre: String = "",
     val email: String = "",
     val password: String = "",
     val confirmarPassword: String = "",
     val errores: ErroresRegistroUsuario = ErroresRegistroUsuario()
)