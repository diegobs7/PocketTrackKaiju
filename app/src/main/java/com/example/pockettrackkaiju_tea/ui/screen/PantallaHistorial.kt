package com.example.pockettrackkaiju_tea.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pockettrackkaiju_tea.model.Movimiento
import com.example.pockettrackkaiju_tea.model.TipoMovimiento
import com.example.pockettrackkaiju_tea.viewmodel.InventarioViewModel

@Composable
fun PantallaHistorial(viewModel: InventarioViewModel) {
    val movimientos by viewModel.movimientos.collectAsStateWithLifecycle()
    val productos by viewModel.productos.collectAsStateWithLifecycle()

    if (movimientos.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Aún no hay movimientos registrados.",
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(movimientos, key = { it.id }) { movimiento ->
                val nombre = productos.find { it.codigo == movimiento.codigoProducto }?.nombre
                TarjetaMovimiento(movimiento, nombre)
            }
        }
    }
}

@Composable
private fun TarjetaMovimiento(movimiento: Movimiento, nombreProducto: String?) {
    val efecto = efectoEnStock(movimiento)
    val colorEfecto = if (efecto >= 0) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.error
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = etiquetaTipo(movimiento.tipo),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if (efecto > 0) "+$efecto" else "$efecto",
                    style = MaterialTheme.typography.titleLarge,
                    color = colorEfecto
                )
            }
            Text(
                text = "${movimiento.codigoProducto} - ${nombreProducto ?: "(producto no encontrado)"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Motivo: ${movimiento.motivo}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "Responsable: ${movimiento.responsable}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun efectoEnStock(movimiento: Movimiento): Int = when (movimiento.tipo) {
    TipoMovimiento.ENTRADA, TipoMovimiento.AJUSTE -> movimiento.cantidad
    TipoMovimiento.SALIDA_VENTA, TipoMovimiento.SALIDA_CONSUMO -> -movimiento.cantidad
}