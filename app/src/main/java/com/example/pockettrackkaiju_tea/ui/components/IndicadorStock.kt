package com.example.pockettrackkaiju_tea.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IndicadorStock(
    stockActual: Int,
    enAlerta: Boolean,
    modifier: Modifier = Modifier
) {
    val colorFondo = if (enAlerta) MaterialTheme.colorScheme.errorContainer
    else MaterialTheme.colorScheme.surface
    val colorTexto = if (enAlerta) MaterialTheme.colorScheme.onErrorContainer
    else MaterialTheme.colorScheme.onSurface
    val colorIcono = if (enAlerta) MaterialTheme.colorScheme.error
    else MaterialTheme.colorScheme.primary

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = colorFondo
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (enAlerta) Icons.Filled.Warning else Icons.Filled.CheckCircle,
                contentDescription = null, // el texto de al lado ya lo dice
                tint = colorIcono,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (enAlerta) "Stock bajo: $stockActual" else "Stock: $stockActual",
                color = colorTexto,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}