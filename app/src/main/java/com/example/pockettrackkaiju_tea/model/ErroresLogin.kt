package com.example.pockettrackkaiju_tea.model

data class ErroresLogin(
    val errorEmail: String? = null,
    val errorPassword: String? = null,
    val errorServidor: String? = null
)