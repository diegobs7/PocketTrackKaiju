package com.example.pockettrackkaiju_tea

import com.example.pockettrackkaiju_tea.repository.DatosDePrueba
import org.junit.Test

class DatosDePruebaTest {

    @Test
    fun verificarDatos() {
        val datos = DatosDePrueba()

        println("=== USUARIOS ===")
        datos.obtenerUsuarios().forEach { println("${it.nombre}: ${it.rol}") }

        println("\n=== PRODUCTOS CON STOCK BAJO ===")
        datos.productos
            .filter { it.stockActual <= it.stockMinimo }
            .forEach { println("${it.nombre} (Stock: ${it.stockActual})") }
    }
}