package com.example.pockettrackkaiju_tea.ui.components

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

enum class ModoCamara {
    ESCANEAR_CODIGO,

    TOMAR_FOTO
}

@Composable
fun CamaraNativa(
    modo: ModoCamara,
    onCerrar: () -> Unit,
    onCodigoEscaneado: (String) -> Unit = {},
    archivoDestino: File? = null,
    onFotoCapturada: () -> Unit = {}
) {
    val contexto = LocalContext.current

    fun tienePermiso() =
        ContextCompat.checkSelfPermission(contexto, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED

    var permisoConcedido by remember { mutableStateOf(tienePermiso()) }
    var yaSePidio by remember { mutableStateOf(false) }

    val solicitarPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        permisoConcedido = concedido
        yaSePidio = true
    }

    LaunchedEffect(Unit) {
        if (!permisoConcedido) solicitarPermiso.launch(Manifest.permission.CAMERA)
    }

    // Si el usuario vuelve desde los ajustes del sistema con el permiso ya dado.
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        permisoConcedido = tienePermiso()
    }

    Dialog(
        onDismissRequest = onCerrar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
            if (permisoConcedido) {
                VistaCamara(
                    modo = modo,
                    onCerrar = onCerrar,
                    onCodigoEscaneado = onCodigoEscaneado,
                    archivoDestino = archivoDestino,
                    onFotoCapturada = onFotoCapturada
                )
            } else {
                val actividad = contexto.buscarActividad()
                val denegadoParaSiempre = yaSePidio && actividad != null &&
                        !ActivityCompat.shouldShowRequestPermissionRationale(
                            actividad, Manifest.permission.CAMERA
                        )
                PermisoDenegado(
                    denegadoParaSiempre = denegadoParaSiempre,
                    onReintentar = { solicitarPermiso.launch(Manifest.permission.CAMERA) },
                    onAbrirAjustes = {
                        contexto.startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", contexto.packageName, null)
                            )
                        )
                    },
                    onCerrar = onCerrar
                )
            }
        }
    }
}

@OptIn(ExperimentalGetImage::class)
@Composable
private fun VistaCamara(
    modo: ModoCamara,
    onCerrar: () -> Unit,
    onCodigoEscaneado: (String) -> Unit,
    archivoDestino: File?,
    onFotoCapturada: () -> Unit
) {
    val contexto = LocalContext.current
    val propietarioCicloVida = LocalLifecycleOwner.current

    val codigoEscaneadoActual by rememberUpdatedState(onCodigoEscaneado)
    val fotoCapturadaActual by rememberUpdatedState(onFotoCapturada)

    val vistaPrevia = remember {
        PreviewView(contexto).apply { scaleType = PreviewView.ScaleType.FILL_CENTER }
    }
    val ejecutor = remember { Executors.newSingleThreadExecutor() }
    val capturaImagen = remember { ImageCapture.Builder().build() }
    val yaEntregado = remember { AtomicBoolean(false) }

    var mensajeError by remember { mutableStateOf<String?>(null) }
    var capturando by remember { mutableStateOf(false) }

    DisposableEffect(modo) {
        var proveedor: ProcessCameraProvider? = null
        var activo = true
        val escaner = BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build()
        )
        val futuro = ProcessCameraProvider.getInstance(contexto)

        futuro.addListener({
            if (!activo) return@addListener
            val camara = futuro.get()
            proveedor = camara

            val preview = Preview.Builder().build().also {
                it.surfaceProvider = vistaPrevia.surfaceProvider
            }

            val casosDeUso: Array<UseCase> = if (modo == ModoCamara.ESCANEAR_CODIGO) {
                val analisis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                analisis.setAnalyzer(ejecutor) { imagenProxy ->
                    val imagenMedia = imagenProxy.image
                    if (imagenMedia == null || yaEntregado.get() || !activo) {
                        imagenProxy.close()
                        return@setAnalyzer
                    }
                    val entrada = InputImage.fromMediaImage(
                        imagenMedia, imagenProxy.imageInfo.rotationDegrees
                    )
                    try {
                        escaner.process(entrada)
                            .addOnSuccessListener { codigos ->
                                val valor = codigos.firstNotNullOfOrNull { it.rawValue }
                                if (valor != null && yaEntregado.compareAndSet(false, true)) {
                                    codigoEscaneadoActual(valor)
                                }
                            }
                            .addOnCompleteListener { imagenProxy.close() }
                    } catch (e: Exception) {
                        imagenProxy.close()
                    }
                }
                arrayOf(preview, analisis)
            } else {
                arrayOf(preview, capturaImagen)
            }

            try {
                camara.unbindAll()
                camara.bindToLifecycle(
                    propietarioCicloVida,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    *casosDeUso
                )
            } catch (e: Exception) {
                mensajeError = "No se pudo iniciar la cámara."
            }
        }, ContextCompat.getMainExecutor(contexto))

        onDispose {
            activo = false
            proveedor?.unbindAll()
            escaner.close()
            ejecutor.shutdown()
        }
    }

    val tomarFoto: () -> Unit = tomarFoto@{
        val destino = archivoDestino ?: return@tomarFoto
        if (capturando) return@tomarFoto
        capturando = true
        val temporal = File(contexto.cacheDir, "captura_${System.currentTimeMillis()}.jpg")
        capturaImagen.takePicture(
            ImageCapture.OutputFileOptions.Builder(temporal).build(),
            ejecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(resultado: ImageCapture.OutputFileResults) {
                    try {
                        destino.parentFile?.mkdirs()
                        temporal.copyTo(destino, overwrite = true)
                        temporal.delete()
                        vistaPrevia.post { fotoCapturadaActual() }
                    } catch (e: IOException) {
                        vistaPrevia.post {
                            capturando = false
                            mensajeError = "No se pudo guardar la foto."
                        }
                    }
                }

                override fun onError(excepcion: ImageCaptureException) {
                    vistaPrevia.post {
                        capturando = false
                        mensajeError = "No se pudo tomar la foto."
                    }
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { vistaPrevia }, modifier = Modifier.fillMaxSize())

        if (modo == ModoCamara.ESCANEAR_CODIGO) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(280.dp)
                    .height(180.dp)
                    .border(BorderStroke(3.dp, Color.White), RoundedCornerShape(16.dp))
            )
        }

        IconButton(
            onClick = onCerrar,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Close, contentDescription = "Cerrar cámara", tint = Color.White)
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            mensajeError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(8.dp)
                )
            }
            Text(
                text = if (modo == ModoCamara.ESCANEAR_CODIGO)
                    "Apunta al código de barras o QR del producto"
                else
                    "Encuadra el producto y presiona el botón",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(8.dp)
            )
            if (modo == ModoCamara.TOMAR_FOTO) {
                Button(
                    onClick = tomarFoto,
                    enabled = !capturando,
                    modifier = Modifier.size(72.dp),
                    shape = RoundedCornerShape(36.dp)
                ) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = "Tomar foto")
                }
            }
        }
    }
}

@Composable
private fun PermisoDenegado(
    denegadoParaSiempre: Boolean,
    onReintentar: () -> Unit,
    onAbrirAjustes: () -> Unit,
    onCerrar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.CameraAlt,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(56.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Permiso de cámara necesario",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "PocketTrack usa la cámara para escanear el código de barras/QR " +
                    "de los productos y para tomar su foto. Sin el permiso puedes seguir " +
                    "usando la app ingresando el código de forma manual.",
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        if (denegadoParaSiempre) {
            Button(onClick = onAbrirAjustes, modifier = Modifier.fillMaxWidth()) {
                Text("Abrir ajustes")
            }
        } else {
            Button(onClick = onReintentar, modifier = Modifier.fillMaxWidth()) {
                Text("Conceder permiso")
            }
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onCerrar, modifier = Modifier.fillMaxWidth()) {
            Text("Ingresar manualmente")
        }
    }
}

private tailrec fun Context.buscarActividad(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.buscarActividad()
    else -> null
}