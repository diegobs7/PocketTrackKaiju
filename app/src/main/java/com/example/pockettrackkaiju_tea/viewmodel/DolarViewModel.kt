package com.example.pockettrackkaiju_tea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockettrackkaiju_tea.repository.DolarRepository
import com.example.pockettrackkaiju_tea.repository.ValorDolar
import com.google.gson.JsonParseException
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class DolarUiState(
    val cargando: Boolean = false,
    val dolar: ValorDolar? = null,
    val error: String? = null
)

class DolarViewModel(private val repository: DolarRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(DolarUiState())
    val uiState: StateFlow<DolarUiState> = _uiState.asStateFlow()

    init {
        cargar()
    }

    fun cargar(forzar: Boolean = false) {
        if (_uiState.value.cargando) return
        _uiState.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            try {
                val dolar = repository.obtenerDolar(forzar)
                _uiState.update { it.copy(cargando = false, dolar = dolar) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                _uiState.update { it.copy(cargando = false, error = "Sin conexión: no se pudo consultar el dólar.") }
            } catch (e: HttpException) {
                _uiState.update { it.copy(cargando = false, error = "El servicio del dólar respondió con error ${e.code()}.") }
            } catch (e: JsonParseException) {
                _uiState.update { it.copy(cargando = false, error = "Respuesta inválida del servicio del dólar.") }
            } catch (e: IllegalStateException) {
                _uiState.update { it.copy(cargando = false, error = e.message ?: "No hay valor del dólar disponible.") }
            }
        }
    }
}
