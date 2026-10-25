package com.example.pockettrackkaiju_tea.ui.components

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.example.pockettrackkaiju_tea.util.NotificacionesStock

@Composable
fun AvisoNotificaciones(modifier: Modifier = Modifier) {
    val contexto = LocalContext.current
    var habilitadas by remember { mutableStateOf(NotificacionesStock.estanHabilitadas(contexto)) }

    // se vuelve a comprobar al regresar desde los ajustes del sistema.
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        habilitadas = NotificacionesStock.estanHabilitadas(contexto)
    }

    val pedirPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        habilitadas = NotificacionesStock.estanHabilitadas(contexto)
        if (!concedido) {
            // Si Android ya no permite volver a preguntar, se lleva al usuario a los ajustes.
            val actividad = contexto.buscarActividad()
            val sePuedeVolverAPreguntar = actividad != null &&
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        actividad, Manifest.permission.POST_NOTIFICATIONS
                    )
            if (!sePuedeVolverAPreguntar) NotificacionesStock.abrirAjustes(contexto)
        }
    }

    if (habilitadas) return

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Notificaciones desactivadas",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Verás los productos con stock bajo en esta pantalla, pero la app no " +
                        "te avisará cuando un producto llegue al stock mínimo.",
                style = MaterialTheme.typography.bodyMedium
            )
            Button(onClick = {
                val faltaPermiso = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        ContextCompat.checkSelfPermission(
                            contexto, Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                if (faltaPermiso) {
                    pedirPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    NotificacionesStock.abrirAjustes(contexto)
                }
            }) {
                Text("Activar notificaciones")
            }
        }
    }
}

private tailrec fun Context.buscarActividad(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.buscarActividad()
    else -> null
}
