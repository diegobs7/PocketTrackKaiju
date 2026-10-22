package com.example.pockettrackkaiju_tea.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.pockettrackkaiju_tea.model.Usuario

@Composable
fun DialogoEliminarUsuario(
    usuario: Usuario,
    tieneMovimientos: Boolean,
    onConfirmar: () -> Unit,
    onCerrar: () -> Unit
) {
    if (tieneMovimientos) {
        AlertDialog(
            onDismissRequest = onCerrar,
            title = { Text("No se puede eliminar") },
            text = {
                Text("\"${usuario.nombre}\" ya registró movimientos. Se conserva para no perder quién los hizo.")
            },
            confirmButton = {
                TextButton(onClick = onCerrar) { Text("Entendido") }
            }
        )
    } else {
        AlertDialog(
            onDismissRequest = onCerrar,
            title = { Text("Eliminar usuario") },
            text = {
                Text("¿Seguro que quieres eliminar a \"${usuario.nombre}\"? Esta acción no se puede deshacer.")
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