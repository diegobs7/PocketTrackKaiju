package com.example.pockettrackkaiju_tea.repository

import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.model.Movimiento
import com.example.pockettrackkaiju_tea.model.Usuario

class InventarioRepository {

    private val productos = DatosDePrueba.productos.toMutableList()

    private val movimientos = DatosDePrueba.movimientos.toMutableList()

    private val usuarios= DatosDePrueba.usuarios.toMutableList()

    // para el ViewModel(esto crea una copia de solo lectura)

    fun obtenerProductos(): List<Producto> = productos.toList()

    fun obtenerMovimientos(): List<Movimiento> = movimientos.toList()

    fun obtenerUsuarios(): List<Usuario> = usuarios.toList()

    //Buscar producto por codigo

    fun buscarProducto(codigo: String): Producto? {
        return productos.find {it.codigo.equals(codigo, ignoreCase = true)}
    }

    //Lista de productos con bajo stock

    fun obtenerProductosStockBajo(): List<Producto> {
        return productos.filter { it.stockActual <= it.stockMinimo }
    }


    //Agregar producto
    fun agregarProducto(nuevoProducto: Producto) {
        val buscado: Producto? = buscarProducto(nuevoProducto.codigo)

        if(buscado==null){
            productos.add(nuevoProducto)
        }
    }

    fun actualizarProducto(productoActualizado: Producto) {
        val index = productos.indexOfFirst { it.codigo.equals(productoActualizado.codigo, ignoreCase = true)}
        if (index != -1) {
            productos[index] = productoActualizado
        }
    }

    fun borrarProducto(codigoProducto: String){
        /*
        version estilo Java
        val buscado: Producto?=buscarProducto(codigoProducto)
        if (buscado!=null) _productos.remove(buscado)
         */
        productos.removeAll{it.codigo.equals(codigoProducto,ignoreCase=true)}//con sintaxis de Kotlin
    }


    //agregar CRUD de movimientos




}

