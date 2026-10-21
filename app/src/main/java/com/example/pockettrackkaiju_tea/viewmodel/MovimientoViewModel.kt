package com.example.pockettrackkaiju_tea.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockettrackkaiju_tea.model.ErroresMovimiento
import com.example.pockettrackkaiju_tea.model.FormularioMovimiento
import com.example.pockettrackkaiju_tea.model.Movimiento
import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.model.TipoMovimiento
import com.example.pockettrackkaiju_tea.model.Usuario
import com.example.pockettrackkaiju_tea.repository.MovimientoRepository
import com.example.pockettrackkaiju_tea.repository.ProductoRepository
import com.example.pockettrackkaiju_tea.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MovimientoViewModel(
    private val productoRepository: ProductoRepository,
    private val movimientoRepository: MovimientoRepository,
    usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _estadoFormulario = MutableStateFlow(FormularioMovimiento())
    val estadoFormulario: StateFlow<FormularioMovimiento> = _estadoFormulario.asStateFlow()

    val productos: StateFlow<List<Producto>> = productoRepository.productos
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val usuarios: StateFlow<List<Usuario>> = usuarioRepository.usuarios
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        viewModelScope.launch { usuarioRepository.cargarUsuariosDePrueba() }
    }

    fun seleccionarProducto(valor: Producto) = _estadoFormulario.update { it.copy(producto = valor) }
    fun seleccionarTipo(valor: TipoMovimiento) = _estadoFormulario.update { it.copy(tipo = valor) }
    fun actualizarCantidad(valor: String) = _estadoFormulario.update { it.copy(cantidad = valor) }
    fun actualizarMotivo(valor: String) = _estadoFormulario.update { it.copy(motivo = valor) }
    fun seleccionarResponsable(valor: Usuario) = _estadoFormulario.update { it.copy(responsable = valor) }

    fun validarFormulario(): Boolean {
        val estado = _estadoFormulario.value
        val cantidadNumerica = estado.cantidad.trim().toIntOrNull()

        val errorProducto = if (estado.producto == null) "Selecciona un producto." else null
        val errorTipo = if (estado.tipo == null) "Selecciona el tipo de movimiento." else null
        val errorMotivo = if (estado.motivo.isBlank()) "El motivo es obligatorio." else null
        val errorResponsable = if (estado.responsable == null) "Selecciona un responsable." else null

        val errorCantidad = when {
            estado.cantidad.isBlank() -> "La cantidad es obligatoria."
            cantidadNumerica == null -> "La cantidad debe ser un numero entero."
            estado.tipo == TipoMovimiento.AJUSTE && cantidadNumerica == 0 ->
                "El ajuste no puede ser 0."
            estado.tipo != TipoMovimiento.AJUSTE && cantidadNumerica <= 0 ->
                "La cantidad debe ser mayor a 0."
            else -> errorDeStock(estado, cantidadNumerica)
        }

        _estadoFormulario.update {
            it.copy(
                errores = ErroresMovimiento(
                    errorProducto,
                    errorTipo,
                    errorCantidad,
                    errorMotivo,
                    errorResponsable
                )
            )
        }
        return errorProducto == null && errorTipo == null && errorCantidad == null &&
                errorMotivo == null && errorResponsable == null
    }

    fun registrarMovimiento(): Boolean {
        if (!validarFormulario()) return false

        val estado = _estadoFormulario.value
        val producto = estado.producto ?: return false
        val tipo = estado.tipo ?: return false
        val responsable = estado.responsable ?: return false
        val cantidad = estado.cantidad.trim().toInt()

        // Usamos el producto con su stock más reciente
        val productoActual = productos.value.find { it.id == producto.id } ?: producto
        val nuevoStock = productoActual.stockActual + efectoEnStock(tipo, cantidad)

        val movimiento = Movimiento(
            codigoProducto = producto.codigo,
            tipo = tipo,
            cantidad = cantidad,
            motivo = estado.motivo.trim(),
            fecha = fechaActual(),
            responsable = responsable.nombre
        )

        viewModelScope.launch {
            movimientoRepository.guardar(movimiento)
            productoRepository.actualizar(productoActual.copy(stockActual = nuevoStock))
        }

        _estadoFormulario.value = FormularioMovimiento()
        return true
    }

    private fun errorDeStock(estado: FormularioMovimiento, cantidad: Int): String? {
        val producto = estado.producto ?: return null
        val tipo = estado.tipo ?: return null
        val stockActual = productos.value.find { it.id == producto.id }?.stockActual
            ?: producto.stockActual

        return if (stockActual + efectoEnStock(tipo, cantidad) < 0) {
            "No hay esa cantidad disponible. Stock actual: $stockActual"
        } else null
    }

    private fun efectoEnStock(tipo: TipoMovimiento, cantidad: Int): Int = when (tipo) {
        TipoMovimiento.ENTRADA, TipoMovimiento.AJUSTE -> cantidad
        TipoMovimiento.SALIDA_VENTA, TipoMovimiento.SALIDA_CONSUMO -> -cantidad
    }

    private fun fechaActual(): String =
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("America/Santiago")
        }.format(Date())

}