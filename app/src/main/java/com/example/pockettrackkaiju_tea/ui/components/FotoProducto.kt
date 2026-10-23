package com.example.pockettrackkaiju_tea.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.example.pockettrackkaiju_tea.util.FotosProducto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun FotoProducto(
    codigo: String,
    modifier: Modifier = Modifier,
    version: Int = 0
) {
    val contexto = LocalContext.current
    val imagen by produceState<ImageBitmap?>(initialValue = null, codigo, version) {
        value = withContext(Dispatchers.IO) {
            FotosProducto.cargar(contexto, codigo)?.asImageBitmap()
        }
    }
    imagen?.let {
        Image(
            bitmap = it,
            contentDescription = "Foto del producto",
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }
}