package com.example.pockettrackkaiju_tea.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.pockettrackkaiju_tea.model.Producto

@Composable
fun DialogoEliminarProducto(
    producto: Producto,
    tieneMovimientos: Boolean,
    onConfirmar: () -> Unit,
    onCerrar: () -> Unit
) {
    if (tieneMovimientos) {
        AlertDialog(
            onDismissRequest = onCerrar,
            title = { Text("No se puede eliminar") },
            text = {
                Text("\"${producto.nombre}\" ya tiene movimientos registrados. Se conserva para no perder su historial.")
            },
            confirmButton = {
                TextButton(onClick = onCerrar) { Text("Entendido") }
            }
        )
    } else {
        AlertDialog(
            onDismissRequest = onCerrar,
            title = { Text("Eliminar producto") },
            text = {
                Text("¿Seguro que quieres eliminar \"${producto.nombre}\"? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                TextButton(onClick = onConfirmar) {
                    Text("Sí, eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = onCerrar) { Text("Cancelar") }
            }
        )
    }
}