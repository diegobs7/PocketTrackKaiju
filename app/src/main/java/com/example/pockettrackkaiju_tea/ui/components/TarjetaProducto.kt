package com.example.pockettrackkaiju_tea.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.ui.theme.PocketTrackKaiju_TEATheme

@Composable
fun TarjetaProducto(
    producto: Producto,
    enAlerta: Boolean,
    onClick: () -> Unit = {} // se conectará al detalle (RF04) más adelante
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = producto.nombre,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${producto.codigo} · ${producto.categoria}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatearPrecio(producto.precio),
                    style = MaterialTheme.typography.titleMedium
                )
                IndicadorStock(stockActual = producto.stockActual, enAlerta = enAlerta)
            }
        }
    }
}

// 1490 -> "$1.490" (pesos chilenos, sin decimales)
private fun formatearPrecio(precio: Int): String =
    "$" + "%,d".format(precio).replace(',', '.')

@Preview(showBackground = true)
@Composable
fun PreviewTarjetaProducto() {
    val ejemplo = Producto(
        codigo = "P-001",
        nombre = "Arroz Grado 1 1kg",
        descripcion = "",
        categoria = "Abarrotes",
        tipo = "Alimento",
        precio = 1490,
        stockActual = 25,
        stockMinimo = 10
    )
    PocketTrackKaiju_TEATheme {
        Column {
            TarjetaProducto(producto = ejemplo, enAlerta = false)
            TarjetaProducto(producto = ejemplo.copy(stockActual = 2), enAlerta = true)
        }
    }
}