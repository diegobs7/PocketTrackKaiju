package com.example.pockettrackkaiju_tea

import com.example.pockettrackkaiju_tea.database.dao.MovimientoDao
import com.example.pockettrackkaiju_tea.database.dao.ProductoDao
import com.example.pockettrackkaiju_tea.database.dao.UsuarioDao
import com.example.pockettrackkaiju_tea.model.CategoriaProducto
import com.example.pockettrackkaiju_tea.model.Movimiento
import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.model.TipoMovimiento
import com.example.pockettrackkaiju_tea.model.TipoProducto
import com.example.pockettrackkaiju_tea.model.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeProductoDao : ProductoDao {
    private val tabla = MutableStateFlow<List<Producto>>(emptyList())
    private var siguienteId = 1

    val lista: List<Producto> get() = tabla.value

    fun agregarDirecto(producto: Producto): Producto {
        val guardado = producto.copy(id = siguienteId++)
        tabla.update { it + guardado }
        return guardado
    }

    override fun obtenerTodos(): Flow<List<Producto>> = tabla

    override suspend fun insertar(producto: Producto) {
        agregarDirecto(producto)
    }

    override suspend fun actualizar(producto: Producto) {
        tabla.update { lista -> lista.map { if (it.id == producto.id) producto else it } }
    }

    override suspend fun eliminar(producto: Producto) {
        tabla.update { lista -> lista.filterNot { it.id == producto.id } }
    }
}

class FakeUsuarioDao : UsuarioDao {
    private val tabla = MutableStateFlow<List<Usuario>>(emptyList())
    private var siguienteId = 1

    val lista: List<Usuario> get() = tabla.value

    fun agregarDirecto(usuario: Usuario): Usuario {
        val guardado = usuario.copy(id = siguienteId++)
        tabla.update { it + guardado }
        return guardado
    }

    override fun obtenerTodos(): Flow<List<Usuario>> = tabla

    override suspend fun insertar(usuario: Usuario) {
        agregarDirecto(usuario)
    }

    override suspend fun eliminar(usuario: Usuario) {
        tabla.update { lista -> lista.filterNot { it.id == usuario.id } }
    }

    override suspend fun contar(): Int = tabla.value.size

    override suspend fun obtenerPorCorreo(correo: String): Usuario? =
        tabla.value.find { it.correo == correo }

    override suspend fun autenticar(correo: String, contra: String): Usuario? =
        tabla.value.find { it.correo == correo && it.contra == contra }

    override suspend fun obtenerPorId(id: Int): Usuario? =
        tabla.value.find { it.id == id }

    override suspend fun cambiarRol(id: Int, rol: Rol) {
        tabla.update { lista -> lista.map { if (it.id == id) it.copy(rol = rol) else it } }
    }
}

class FakeMovimientoDao : MovimientoDao {
    private val tabla = MutableStateFlow<List<Movimiento>>(emptyList())
    private var siguienteId = 1

    val lista: List<Movimiento> get() = tabla.value

    fun agregarDirecto(movimiento: Movimiento): Movimiento {
        val guardado = movimiento.copy(id = siguienteId++)
        tabla.update { it + guardado }
        return guardado
    }

    override fun obtenerTodos(): Flow<List<Movimiento>> = tabla

    override fun obtenerPorProducto(codigo: String): Flow<List<Movimiento>> =
        tabla.map { lista -> lista.filter { it.codigoProducto == codigo } }

    override suspend fun insertar(movimiento: Movimiento) {
        agregarDirecto(movimiento)
    }
}

// ---- Datos de ejemplo para armar escenarios de prueba ----

fun productoDePrueba(
    codigo: String = "KAI-001",
    stockActual: Int = 10,
    stockMinimo: Int = 5
) = Producto(
    codigo = codigo,
    nombre = "Polera Godzilla",
    descripcion = "Polera de prueba",
    categoria = CategoriaProducto.ASIAN_KAIJU,
    tipo = TipoProducto.POLERA,
    precio = 15990,
    stockActual = stockActual,
    stockMinimo = stockMinimo
)

fun usuarioDePrueba(id: Int = 1, rol: Rol = Rol.ENCARGADO_INVENTARIO) = Usuario(
    id = id,
    nombre = "Diego Bergeret",
    correo = "diego@kaiju.cl",
    contra = "1234",
    rol = rol
)

fun movimientoDePrueba(
    codigoProducto: String,
    tipo: TipoMovimiento = TipoMovimiento.ENTRADA,
    cantidad: Int = 5
) = Movimiento(
    codigoProducto = codigoProducto,
    tipo = tipo,
    cantidad = cantidad,
    motivo = "Prueba",
    fecha = "01/01/2026 10:00",
    responsable = "Diego Bergeret",
    responsableId = 1
)