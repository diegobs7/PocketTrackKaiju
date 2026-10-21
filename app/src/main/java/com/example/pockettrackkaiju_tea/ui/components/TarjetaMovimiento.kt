package com.example.pockettrackkaiju_tea.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pockettrackkaiju_tea.model.Movimiento

@Composable
fun TarjetaMovimiento(
    movimiento: Movimiento,
    nombreProducto: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = etiquetaTipo(movimiento.tipo),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = movimiento.codigoProducto + (nombreProducto?.let { " - $it" } ?: ""),
                style = MaterialTheme.typography.bodyLarge
            )
            Text("Cantidad: ${movimiento.cantidad}")
            Text("Motivo: ${movimiento.motivo}")
            Text(
                text = "Responsable: ${movimiento.responsable}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = movimiento.fecha,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}