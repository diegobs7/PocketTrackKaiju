package com.example.pockettrackkaiju_tea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockettrackkaiju_tea.repository.PokemonRepository
import com.google.gson.JsonParseException
import java.io.IOException
import kotlin.random.Random
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class EasterEggViewModel(
    private val repository: PokemonRepository,
    private val reloj: () -> Long = System::currentTimeMillis
) : ViewModel() {

    private val _uiState = MutableStateFlow(EasterEggUiState())
    val uiState = _uiState.asStateFlow()

    private var toques = 0
    private var instanteUltimoToque = 0L
    private var consultaEnCurso: Job? = null

    fun registrarToque() {
        if (_uiState.value.visible) return
        val ahora = reloj()
        toques = if (toques > 0 && ahora - instanteUltimoToque <= VENTANA_MS) toques + 1 else 1
        instanteUltimoToque = ahora
        if (toques >= TOQUES_NECESARIOS) {
            toques = 0
            _uiState.value = EasterEggUiState(visible = true)
            consultarAleatorio()
        }
    }

    fun otroAleatorio() {
        if (_uiState.value.cargando) return
        consultarAleatorio()
    }

    private fun consultarAleatorio() {
        val id = Random.nextInt(1, MAX_ID + 1).toString()
        _uiState.update { it.copy(consulta = id) }
        buscar()
    }

    fun actualizarConsulta(valor: String) {
        if (_uiState.value.cargando) return
        _uiState.update { it.copy(consulta = valor, error = null, pokemon = null) }
    }

    fun buscar() {
        if (_uiState.value.cargando) return // Impide solicitudes simultáneas.
        val consulta = _uiState.value.consulta.trim()
        if (consulta.isBlank()) {
            _uiState.update { it.copy(error = "Escribe un nombre o número.", pokemon = null) }
            return
        }
        _uiState.update { it.copy(cargando = true, error = null, pokemon = null) }
        consultaEnCurso = viewModelScope.launch {
            try {
                val pokemon = repository.buscar(consulta)
                _uiState.update { it.copy(cargando = false, pokemon = pokemon) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: HttpException) {
                mostrarError(
                    if (e.code() == 404) "No encontré ese Pokémon. Revisa el nombre o número."
                    else "El servidor respondió con error ${e.code()}. Intenta nuevamente."
                )
            } catch (e: IOException) {
                mostrarError("No pude conectar. Revisa internet e intenta nuevamente.")
            } catch (e: JsonParseException) {
                mostrarError("La respuesta no tiene el formato esperado.")
            }
        }
    }

    fun cerrar() {
        consultaEnCurso?.cancel()
        _uiState.value = EasterEggUiState()
    }

    private fun mostrarError(mensaje: String) {
        _uiState.update { it.copy(cargando = false, pokemon = null, error = mensaje) }
    }

    private companion object {
        const val TOQUES_NECESARIOS = 3
        const val VENTANA_MS = 1500L
        const val MAX_ID = 151
    }
}