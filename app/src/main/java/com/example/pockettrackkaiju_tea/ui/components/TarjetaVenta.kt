package com.example.pockettrackkaiju_tea.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pockettrackkaiju_tea.model.VentaProducto

@Composable
fun TarjetaVenta(
    venta: VentaProducto,
    modifier: Modifier = Modifier,
    posicion: Int? = null
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (posicion != null) {
                Text(
                    text = "$posicion",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(Modifier.width(16.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = venta.producto.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${venta.producto.codigo} · ${venta.producto.categoria.displayName}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${venta.unidades}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "vendidas",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}