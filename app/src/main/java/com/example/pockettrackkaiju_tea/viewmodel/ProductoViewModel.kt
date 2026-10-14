package com.example.pockettrackkaiju_tea.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pockettrackkaiju_tea.model.ErroresProducto
import com.example.pockettrackkaiju_tea.model.FormularioProducto
import com.example.pockettrackkaiju_tea.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProductoViewModel : ViewModel() {

    private val _estadoFormulario = MutableStateFlow(FormularioProducto())
    val estadoFormulario: StateFlow<FormularioProducto> = _estadoFormulario.asStateFlow()

    fun actualizarCodigo(valor: String) = _estadoFormulario.update { it.copy(codigo = valor) }
    fun actualizarNombre(valor: String) = _estadoFormulario.update { it.copy(nombre = valor) }
    fun actualizarDescripcion(valor: String) = _estadoFormulario.update { it.copy(descripcion = valor) }
    fun actualizarCategoria(valor: String) = _estadoFormulario.update { it.copy(categoria = valor) }
    fun actualizarTipo(valor: String) = _estadoFormulario.update { it.copy(tipo = valor) }
    fun actualizarPrecio(valor: String) = _estadoFormulario.update { it.copy(precio = valor) }
    fun actualizarStockActual(valor: String) = _estadoFormulario.update { it.copy(stockActual = valor) }
    fun actualizarDetalle(valor: String) = _estadoFormulario.update { it.copy(detalle = valor) }

    fun validarFormulario(codigosExistentes: List<String>): Boolean {
        val estado = _estadoFormulario.value
        val precioNumerico = estado.precio.trim().toIntOrNull()
        val stockNumerico = estado.stockActual.trim().toIntOrNull()

        val errorCodigo = when {
            estado.codigo.isBlank() -> "El codigo es obligatorio."
            codigosExistentes.any { it.equals(estado.codigo.trim(), ignoreCase = true) } ->
                "Ya existe un producto con ese codigo."
            else -> null
        }
        val errorNombre = if (estado.nombre.isBlank()) "El nombre es obligatorio." else null
        val errorDescripcion = if (estado.descripcion.isBlank()) "La descripcion es obligatoria." else null
        val errorCategoria = if (estado.categoria.isBlank()) "La categoria es obligatoria." else null
        val errorTipo = if (estado.tipo.isBlank()) "El tipo es obligatorio." else null

        val errorPrecio = when {
            estado.precio.isBlank() -> "El precio es obligatorio."
            precioNumerico == null -> "El precio debe ser un numero entero."
            precioNumerico <= 0 -> "El precio debe ser mayor a 0."
            else -> null
        }
        val errorStockActual = when {
            estado.stockActual.isBlank() -> "El stock es obligatorio."
            stockNumerico == null -> "El stock debe ser un numero entero."
            stockNumerico < 0 -> "El stock no puede ser negativo."
            else -> null
        }

        _estadoFormulario.update {
            it.copy(
                errores = ErroresProducto(
                    errorCodigo,
                    errorNombre,
                    errorDescripcion,
                    errorCategoria,
                    errorTipo,
                    errorPrecio,
                    errorStockActual
                )
            )
        }
        return errorCodigo == null && errorNombre == null && errorDescripcion == null &&
                errorCategoria == null && errorTipo == null &&
                errorPrecio == null && errorStockActual == null
    }


    fun crearProducto(codigosExistentes: List<String>): Producto? {
        if (!validarFormulario(codigosExistentes)) return null

        val estado = _estadoFormulario.value
        val producto = Producto(
            codigo = estado.codigo.trim(),
            nombre = estado.nombre.trim(),
            descripcion = estado.descripcion.trim(),
            categoria = estado.categoria.trim(),
            tipo = estado.tipo.trim(),
            precio = estado.precio.trim().toInt(),
            stockActual = estado.stockActual.trim().toInt(),
            detalle = estado.detalle.trim().ifBlank { null }
        )

        _estadoFormulario.value = FormularioProducto()   // limpia el formulario
        return producto
    }
}