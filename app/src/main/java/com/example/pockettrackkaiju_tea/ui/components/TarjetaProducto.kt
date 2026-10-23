package com.example.pockettrackkaiju_tea.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.pockettrackkaiju_tea.model.CategoriaProducto
import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.model.TipoProducto
import com.example.pockettrackkaiju_tea.ui.theme.PocketTrackKaiju_TEATheme

@Composable
fun TarjetaProducto(
    producto: Producto,
    enAlerta: Boolean,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = producto.nombre,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${producto.codigo} · ${producto.categoria}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                FotoProducto(
                    codigo = producto.codigo,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

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


private fun formatearPrecio(precio: Int): String =
    "$" + "%,d".format(precio).replace(',', '.')

@Composable
fun PreviewTarjetaProducto() {
    val ejemplo = Producto(
        codigo = "KAI-008",
        nombre = "Polera Sin Mangas Evangelion",
        descripcion = "Polera sin mangas ideal para verano, diseño EVA-01",
        categoria = CategoriaProducto.ANIME,
        tipo = TipoProducto.POLERA_SIN_MANGAS,
        precio = 14990,
        stockActual = 6,
        stockMinimo = 8,
        detalle = null
    )
    PocketTrackKaiju_TEATheme {
        Column {
            TarjetaProducto(producto = ejemplo, enAlerta = false)
            TarjetaProducto(producto = ejemplo.copy(stockActual = 2), enAlerta = true)
        }
    }
}