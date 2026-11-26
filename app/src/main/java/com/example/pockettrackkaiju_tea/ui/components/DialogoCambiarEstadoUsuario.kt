package com.example.pockettrackkaiju_tea.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.pockettrackkaiju_tea.model.Usuario

@Composable
fun DialogoCambiarEstadoUsuario(
    usuario: Usuario,
    onConfirmar: () -> Unit,
    onCerrar: () -> Unit
) {
    val desactivando = usuario.activo

    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text(if (desactivando) "Desactivar usuario" else "Activar usuario") },
        text = {
            Text(
                if (desactivando) {
                    "¿Seguro que quieres desactivar a \"${usuario.nombre}\"? " +
                            "No podrá iniciar sesión hasta que lo actives de nuevo."
                } else {
                    "¿Quieres activar a \"${usuario.nombre}\"? " +
                            "Podrá volver a iniciar sesión."
                }
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text(if (desactivando) "Sí, desactivar" else "Sí, activar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCerrar) { Text("Cancelar") }
        }
    )
}