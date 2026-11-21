package com.example.pockettrackkaiju_tea.ui.components

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun OpcionMenu(
    texto: String,
    seleccionada: Boolean,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(
                text = texto,
                color = if (seleccionada) MaterialTheme.colorScheme.onPrimaryContainer else Color.Unspecified,
                fontWeight = if (seleccionada) FontWeight.Bold else null
            )
        },
        onClick = onClick,
        modifier = Modifier.background(
            if (seleccionada) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
        )
    )
}