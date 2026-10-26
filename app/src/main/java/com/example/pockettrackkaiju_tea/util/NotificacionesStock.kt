package com.example.pockettrackkaiju_tea.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.pockettrackkaiju_tea.MainActivity
import com.example.pockettrackkaiju_tea.R
import com.example.pockettrackkaiju_tea.model.Producto

/**
 * Notificaciones de stock bajo.
 *
 * - Canal "stock_bajo" (obligatorio desde Android 8).
 * - Permiso POST_NOTIFICATIONS en tiempo de ejecución desde Android 13.
 * - Una notificación por producto: si el stock sigue bajando se actualiza la misma
 *   (no se acumulan) y se elimina cuando el producto se repone.
 */
object NotificacionesStock {

    const val CANAL_ID = "stock_bajo"

    fun crearCanal(contexto: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val canal = NotificationChannel(
            CANAL_ID,
            "Alertas de stock bajo",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Avisa cuando un producto llega a su stock mínimo."
        }
        contexto.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(canal)
    }

    /** true si la app tiene permiso (Android 13+) y las notificaciones no están bloqueadas. */
    fun estanHabilitadas(contexto: Context): Boolean {
        val permiso = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(
                    contexto, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
        return permiso && NotificationManagerCompat.from(contexto).areNotificationsEnabled()
    }

    /** Envía la notificación si el producto está en alerta; la quita si ya se repuso. */
    fun revisar(contexto: Context, producto: Producto) {
        if (producto.stockActual <= producto.stockMinimo) {
            notificarStockBajo(contexto, producto)
        } else {
            cancelar(contexto, producto)
        }
    }

    @SuppressLint("MissingPermission")
    fun notificarStockBajo(contexto: Context, producto: Producto): Boolean {
        // Si el usuario rechazó el permiso no se envía nada; la app sigue funcionando
        // y las alertas se ven igual en la pestaña "Alertas".
        if (!estanHabilitadas(contexto)) return false
        crearCanal(contexto)

        val id = idNotificacion(producto)
        val abrirApp = PendingIntent.getActivity(
            contexto,
            id,
            Intent(contexto, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val texto = "Quedan ${producto.stockActual} unidades (mínimo: ${producto.stockMinimo})."
        val notificacion = NotificationCompat.Builder(contexto, CANAL_ID)
            .setSmallIcon(R.drawable.ic_notificacion_stock)
            .setContentTitle("Stock bajo: ${producto.nombre}")
            .setContentText(texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText("${producto.codigo} · $texto"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setContentIntent(abrirApp)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(contexto).notify(id, notificacion)
        return true
    }

    fun cancelar(contexto: Context, producto: Producto) {
        NotificationManagerCompat.from(contexto).cancel(idNotificacion(producto))
    }

    /** Abre los ajustes de notificaciones de la app (para cuando el usuario las rechazó). */
    fun abrirAjustes(contexto: Context) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, contexto.packageName)
        } else {
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", contexto.packageName, null)
            )
        }
        contexto.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    // El código identifica al producto incluso antes de que Room le asigne su id.
    private fun idNotificacion(producto: Producto): Int = producto.codigo.trim().lowercase().hashCode()
}
