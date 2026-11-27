package com.example.pockettrackkaiju_tea

import com.example.pockettrackkaiju_tea.util.ConfigServidor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ConfigServidorTest {

    @Test
    fun agregaEsquemaYBarraFinalAUnaIpConPuerto() {
        assertEquals("http://54.12.34.56:8080/", ConfigServidor.normalizar("54.12.34.56:8080"))
    }

    @Test
    fun respetaHttpsYNoDuplicaLaBarra() {
        assertEquals("https://mi.dominio.cl/", ConfigServidor.normalizar("  https://mi.dominio.cl// "))
    }

    @Test
    fun aceptaUnBackendLocal() {
        assertEquals("http://10.0.2.2:8080/", ConfigServidor.normalizar("http://10.0.2.2:8080"))
    }

    @Test
    fun rechazaTextoVacioOConEspacios() {
        assertNull(ConfigServidor.normalizar("   "))
        assertNull(ConfigServidor.normalizar("54.12 .34.56"))
    }

    @Test
    fun rechazaDireccionesSinServidor() {
        assertNull(ConfigServidor.normalizar("http://"))
        assertNull(ConfigServidor.normalizar(":8080"))
    }
}
