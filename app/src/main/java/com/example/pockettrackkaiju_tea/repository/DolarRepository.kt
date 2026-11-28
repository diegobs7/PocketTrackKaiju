package com.example.pockettrackkaiju_tea.repository

import com.example.pockettrackkaiju_tea.remote.DolarApi

data class ValorDolar(val pesos: Double, val fecha: String)

class DolarRepository(private val api: DolarApi) {

    private var ultimo: ValorDolar? = null

    suspend fun obtenerDolar(forzar: Boolean = false): ValorDolar {
        if (!forzar) ultimo?.let { return it }

        val ultimaMedicion = api.obtenerDolar().serie.firstOrNull()
            ?: throw IllegalStateException("La API no entregó valores del dólar.")
        return ValorDolar(ultimaMedicion.valor, ultimaMedicion.fecha.take(10)).also { ultimo = it }
    }
}
