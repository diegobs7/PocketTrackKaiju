package com.example.pockettrackkaiju_tea.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pockettrackkaiju_tea.ui.components.TarjetaMovimiento
import com.example.pockettrackkaiju_tea.ui.components.TarjetaResumen
import com.example.pockettrackkaiju_tea.viewmodel.InventarioViewModel

@Composable
fun PantallaInicio(viewModel: InventarioViewModel) {
    val productos by viewModel.productos.collectAsStateWithLifecycle()
    val movimientos by viewModel.movimientos.collectAsStateWithLifecycle()

    val enAlerta = productos.count { viewModel.estaEnAlerta(it) }
    val ultimosMovimientos = movimientos.take(3)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Resumen del inventario",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TarjetaResumen(
                    titulo = "Productos",
                    valor = productos.size,
                    modifier = Modifier.weight(1f)
                )
                TarjetaResumen(
                    titulo = "En alerta",
                    valor = enAlerta,
                    modifier = Modifier.weight(1f),
                    destacada = enAlerta > 0
                )
                TarjetaResumen(
                    titulo = "Movimientos",
                    valor = movimientos.size,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Text(
                text = "Últimos movimientos",
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (ultimosMovimientos.isEmpty()) {
            item { Text("Aún no hay movimientos registrados.") }
        } else {
            items(ultimosMovimientos, key = { it.id }) { movimiento ->
                TarjetaMovimiento(
                    movimiento = movimiento,
                    nombreProducto = productos.find { it.codigo == movimiento.codigoProducto }?.nombre
                )
            }
        }
    }
}