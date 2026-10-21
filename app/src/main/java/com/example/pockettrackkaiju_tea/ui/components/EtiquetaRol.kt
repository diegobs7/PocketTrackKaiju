package com.example.pockettrackkaiju_tea.ui.components

import com.example.pockettrackkaiju_tea.model.Rol

fun etiquetaRol(rol: Rol): String = when (rol) {
    Rol.ADMINISTRADOR -> "Administrador"
    Rol.VENDEDOR -> "Vendedor"
    Rol.ENCARGADO_INVENTARIO -> "Encargado de inventario"
}