package com.example.pockettrackkaiju_tea.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.File
import kotlin.math.max


object FotosProducto {

    private fun carpeta(contexto: Context): File =
        File(contexto.filesDir, "fotos_productos").apply { mkdirs() }

    fun archivo(contexto: Context, codigo: String): File {
        val nombreSeguro = codigo.trim().replace(Regex("[^A-Za-z0-9_-]"), "_")
        return File(carpeta(contexto), "$nombreSeguro.jpg")
    }

    fun eliminar(contexto: Context, codigo: String) {
        if (codigo.isNotBlank()) archivo(contexto, codigo).delete()
    }

    /** Carga la foto reducida (para no gastar memoria) y corrige su rotación. */
    fun cargar(contexto: Context, codigo: String, ladoMaximo: Int = 800): Bitmap? {
        if (codigo.isBlank()) return null
        val archivo = archivo(contexto, codigo)
        if (!archivo.exists()) return null

        val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(archivo.path, limites)

        var muestreo = 1
        while (max(limites.outWidth, limites.outHeight) / muestreo > ladoMaximo) muestreo *= 2

        val bitmap = BitmapFactory.decodeFile(
            archivo.path,
            BitmapFactory.Options().apply { inSampleSize = muestreo }
        ) ?: return null

        val grados = try {
            when (
                ExifInterface(archivo.path)
                    .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            ) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
        } catch (e: Exception) {
            0f
        }
        if (grados == 0f) return bitmap

        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height,
            Matrix().apply { postRotate(grados) }, true
        )
    }
}