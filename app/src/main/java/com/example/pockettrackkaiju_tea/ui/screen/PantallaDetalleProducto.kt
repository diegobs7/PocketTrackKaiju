package com.example.pockettrackkaiju_tea.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.ui.components.FotoProducto
import com.example.pockettrackkaiju_tea.ui.components.IndicadorStock
import com.example.pockettrackkaiju_tea.ui.components.TarjetaMovimiento
import com.example.pockettrackkaiju_tea.viewmodel.InventarioViewModel

// Pantalla de detalle: la pueden abrir todos los roles tocando un producto del catálogo.
// Solo quien puede gestionar productos (administrador) ve el botón "Editar".
@Composable
fun PantallaDetalleProducto(
    productoId: Int,
    viewModel: InventarioViewModel,
    puedeEditar: Boolean,
    onEditar: (Producto) -> Unit
) {
    // Se observan las dos listas: si cambia el stock o se registra un movimiento,
    // la pantalla se redibuja sola.
    val productos by viewModel.productos.collectAsStateWithLifecycle()
    val movimientos by viewModel.movimientos.collectAsStateWithLifecycle()

    val producto = productos.find { it.id == productoId }

    // Si el producto no existe (por ejemplo, fue eliminado) no hay nada que mostrar.
    if (producto == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "No se encontró el producto.",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    val historial = movimientos.filter { it.codigoProducto.equals(producto.codigo, ignoreCase = true) }
    val enAlerta = viewModel.estaEnAlerta(producto)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Foto: si el producto no tiene, se ve el ícono de fondo; si tiene, la foto lo cubre.
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Inventory2,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(64.dp)
                )
                FotoProducto(
                    codigo = producto.codigo,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        item {
            Column {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${producto.codigo} · ${producto.categoria.displayName}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatearPrecioDetalle(producto.precio),
                    style = MaterialTheme.typography.headlineSmall
                )
                IndicadorStock(stockActual = producto.stockActual, enAlerta = enAlerta)
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilaDetalle("Tipo", producto.tipo.displayName)
                    FilaDetalle("Stock mínimo", producto.stockMinimo.toString())
                    FilaDetalle("Descripción", producto.descripcion)
                    producto.detalle?.let { FilaDetalle("Detalle", it) }
                }
            }
        }

        if (puedeEditar) {
            item {
                Button(
                    onClick = { onEditar(producto) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Editar producto")
                }
            }
        }

        item {
            Text(
                text = "Historial de movimientos (${historial.size})",
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (historial.isEmpty()) {
            item {
                Text(
                    text = "Este producto aún no tiene movimientos.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(historial, key = { it.id }) { movimiento ->
                TarjetaMovimiento(
                    movimiento = movimiento,
                    nombreProducto = producto.nombre
                )
            }
        }
    }
}

@Composable
private fun FilaDetalle(etiqueta: String, valor: String) {
    Column {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(text = valor, style = MaterialTheme.typography.bodyLarge)
    }
}

private fun formatearPrecioDetalle(precio: Int): String =
    "$" + "%,d".format(precio).replace(',', '.')
