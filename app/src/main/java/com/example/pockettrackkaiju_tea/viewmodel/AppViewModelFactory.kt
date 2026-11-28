package com.example.pockettrackkaiju_tea.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pockettrackkaiju_tea.database.AppDatabase
import com.example.pockettrackkaiju_tea.remote.PokeApiClient
import com.example.pockettrackkaiju_tea.repository.MovimientoRepository
import com.example.pockettrackkaiju_tea.repository.PokemonRepository
import com.example.pockettrackkaiju_tea.repository.ProductoRepository
import com.example.pockettrackkaiju_tea.repository.UsuarioRepository
import com.example.pockettrackkaiju_tea.repository.UsuarioRepositoryRemoto
import com.example.pockettrackkaiju_tea.remote.kaiju.KaijuClient
import com.example.pockettrackkaiju_tea.util.ConfigServidor
import com.example.pockettrackkaiju_tea.util.SesionGuardada
import com.example.pockettrackkaiju_tea.remote.DolarApiClient
import com.example.pockettrackkaiju_tea.repository.DolarRepository
class AppViewModelFactory(contexto: Context) : ViewModelProvider.Factory {

    private val appContext = contexto.applicationContext
    private val database = AppDatabase.getInstance(appContext)
    private val productoRepository = ProductoRepository(database.productoDao())
    private val movimientoRepository = MovimientoRepository(database.movimientoDao())
    private val configServidor = ConfigServidor(appContext)
    private val usuarioRepository = repositorioDeUsuarios(appContext, configServidor)
    private val pokemonRepository = PokemonRepository(PokeApiClient.api)

    private val dolarRepository = DolarRepository(DolarApiClient.api)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(ProductoViewModel::class.java) ->
            ProductoViewModel(productoRepository, appContext)

        modelClass.isAssignableFrom(InventarioViewModel::class.java) ->
            InventarioViewModel(productoRepository, movimientoRepository)

        modelClass.isAssignableFrom(MovimientoViewModel::class.java) ->
            MovimientoViewModel(productoRepository, movimientoRepository, appContext)

        modelClass.isAssignableFrom(RegistroUsuarioViewModel::class.java) ->
            RegistroUsuarioViewModel(usuarioRepository)

        modelClass.isAssignableFrom(LoginViewModel::class.java) ->
            LoginViewModel(usuarioRepository, configServidor)

        modelClass.isAssignableFrom(SesionViewModel::class.java) ->
            SesionViewModel(usuarioRepository, SesionGuardada(appContext))

        modelClass.isAssignableFrom(UsuariosViewModel::class.java) ->
            UsuariosViewModel(usuarioRepository, movimientoRepository)

        modelClass.isAssignableFrom(EasterEggViewModel::class.java) ->
            EasterEggViewModel(pokemonRepository)

        modelClass.isAssignableFrom(EditarUsuarioViewModel::class.java) ->
            EditarUsuarioViewModel(usuarioRepository)

        modelClass.isAssignableFrom(DolarViewModel::class.java) ->
            DolarViewModel(dolarRepository)

        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    } as T

    private companion object {
        // Una sola instancia para toda la app: cada pantalla crea su propia fábrica,
        // y todas deben compartir los mismos usuarios y la misma sesión.
        @Volatile
        private var repositorio: UsuarioRepository? = null

        fun repositorioDeUsuarios(contexto: Context, config: ConfigServidor): UsuarioRepository =
            repositorio ?: synchronized(this) {
                repositorio ?: crearRepositorioRemoto(contexto, config).also { repositorio = it }
            }

        private fun crearRepositorioRemoto(contexto: Context, config: ConfigServidor): UsuarioRepository {
            val sesion = SesionGuardada(contexto)
            // El cliente necesita el token del repositorio y el repositorio necesita la api del cliente.
            lateinit var remoto: UsuarioRepositoryRemoto
            val cliente = KaijuClient(config) { remoto.tokenActual() }
            remoto = UsuarioRepositoryRemoto(cliente.api, sesion)
            return remoto
        }
    }
}