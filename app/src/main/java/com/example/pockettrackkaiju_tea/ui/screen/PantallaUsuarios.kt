package com.example.pockettrackkaiju_tea.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pockettrackkaiju_tea.model.Usuario
import com.example.pockettrackkaiju_tea.ui.components.DialogoCambiarRol
import com.example.pockettrackkaiju_tea.ui.components.DialogoEliminarUsuario
import com.example.pockettrackkaiju_tea.ui.components.TarjetaUsuario
import com.example.pockettrackkaiju_tea.viewmodel.AppViewModelFactory
import com.example.pockettrackkaiju_tea.viewmodel.UsuariosViewModel

@Composable
fun PantallaUsuarios(
    usuarioActual: Usuario,
    viewModel: UsuariosViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
) {
    val usuarios by viewModel.usuarios.collectAsStateWithLifecycle()
    var usuarioAEditar by remember { mutableStateOf<Usuario?>(null) }
    var usuarioAEliminar by remember { mutableStateOf<Usuario?>(null) }

    usuarioAEditar?.let { usuario ->
        DialogoCambiarRol(
            usuario = usuario,
            onGuardar = { nuevoRol ->
                viewModel.cambiarRol(usuario, nuevoRol)
                usuarioAEditar = null
            },
            onCerrar = { usuarioAEditar = null }
        )
    }

    usuarioAEliminar?.let { usuario ->
        DialogoEliminarUsuario(
            usuario = usuario,
            tieneMovimientos = viewModel.tieneMovimientos(usuario),
            onConfirmar = {
                viewModel.eliminarUsuario(usuario)
                usuarioAEliminar = null
            },
            onCerrar = { usuarioAEliminar = null }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Usuarios registrados: ${usuarios.size}",
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(usuarios, key = { it.id }) { usuario ->
            TarjetaUsuario(
                usuario = usuario,
                esElMismo = usuario.id == usuarioActual.id,
                onCambiarRol = { usuarioAEditar = usuario },
                onEliminar = {usuarioAEliminar = usuario}
            )
        }
    }
}