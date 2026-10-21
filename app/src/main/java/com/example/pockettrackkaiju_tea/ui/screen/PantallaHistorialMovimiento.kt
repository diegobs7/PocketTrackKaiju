package com.example.pockettrackkaiju_tea.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pockettrackkaiju_tea.ui.components.TarjetaMovimiento
import com.example.pockettrackkaiju_tea.viewmodel.InventarioViewModel

@Composable
fun PantallaHistorialMovimientos(viewModel: InventarioViewModel) {
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
                TarjetaMovimiento(
                    movimiento = movimiento,
                    nombreProducto = productos.find { it.codigo == movimiento.codigoProducto }?.nombre
                )
            }
        }
    }
}