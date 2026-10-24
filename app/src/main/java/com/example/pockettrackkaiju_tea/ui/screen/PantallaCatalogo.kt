package com.example.pockettrackkaiju_tea.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pockettrackkaiju_tea.model.CategoriaProducto
import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.ui.components.DialogoEliminarProducto
import com.example.pockettrackkaiju_tea.ui.components.TarjetaProducto
import com.example.pockettrackkaiju_tea.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCatalogo(
    viewModel: InventarioViewModel,
    esAdministrador: Boolean = false,
    onEditar: (Producto) -> Unit = {},
    onVerDetalle: (Producto) -> Unit = {}
) {
    val productos by viewModel.productos.collectAsStateWithLifecycle()

    var textoBusqueda by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf<CategoriaProducto?>(null) }
    var busquedaExpandida by remember { mutableStateOf(false) }
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }

    var menuCategoriaExpandido by remember { mutableStateOf(false) }

    val productosFiltrados = productos.filter { producto ->
        val coincideTexto = producto.nombre.contains(textoBusqueda, ignoreCase = true) ||
                producto.codigo.contains(textoBusqueda, ignoreCase = true)

        val coincideCategoria = categoriaSeleccionada == null || producto.categoria == categoriaSeleccionada

        coincideTexto && coincideCategoria
    }

    productoAEliminar?.let { producto ->
        DialogoEliminarProducto(
            producto = producto,
            tieneMovimientos = viewModel.tieneMovimientos(producto),
            onConfirmar = {
                viewModel.eliminarProducto(producto)
                productoAEliminar = null
            },
            onCerrar = { productoAEliminar = null }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (busquedaExpandida) {
                    OutlinedTextField(
                        value = textoBusqueda,
                        onValueChange = { textoBusqueda = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Buscar por nombre o código...") },
                        leadingIcon = {
                            IconButton(onClick = {
                                busquedaExpandida = false
                                textoBusqueda = ""
                            }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Cerrar búsqueda")
                            }
                        },
                        trailingIcon = {
                            if (textoBusqueda.isNotEmpty()) {
                                IconButton(onClick = { textoBusqueda = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                                }
                            }
                        },
                        singleLine = true
                    )
                } else {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Catálogo",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        IconButton(
                            onClick = { busquedaExpandida = true },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Abrir búsqueda")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = menuCategoriaExpandido,
                onExpandedChange = { menuCategoriaExpandido = !menuCategoriaExpandido },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = categoriaSeleccionada?.displayName ?: "Todas las categorías",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Filtrar por categoría") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuCategoriaExpandido)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = menuCategoriaExpandido,
                    onDismissRequest = { menuCategoriaExpandido = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Todas las categorías") },
                        onClick = {
                            categoriaSeleccionada = null
                            menuCategoriaExpandido = false
                        }
                    )

                    CategoriaProducto.entries.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria.displayName) },
                            onClick = {
                                categoriaSeleccionada = categoria
                                menuCategoriaExpandido = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (productosFiltrados.isEmpty()) {
                item {
                    Text(
                        text = "No se encontraron productos.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(productosFiltrados, key = { it.codigo }) { producto ->
                    Column {
                        TarjetaProducto(
                            producto = producto,
                            enAlerta = viewModel.estaEnAlerta(producto),
                            onClick = { onVerDetalle(producto) }
                        )
                        if (esAdministrador) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = { onEditar(producto) }) {
                                    Text("Editar")
                                }
                                TextButton(onClick = { productoAEliminar = producto }) {
                                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}