package com.example.pockettrackkaiju_tea.repository

import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.model.Movimiento

class InventarioRepository {

    private val _productos = DatosDePrueba.productos.toMutableList()
    private val _movimientos = DatosDePrueba.movimientos.toMutableList()

    // para el ViewModel(esto crea una copia de solo lectura)

    fun obtenerProductos(): List<Producto> = _productos.toList()

    fun obtenerMovimientos(): List<Movimiento> = _movimientos.toList()


    //Buscar producto por codigo

    fun buscarProducto(codigo: String): Producto? {
        return _productos.find {it.codigo.equals(codigo, ignoreCase = true)}
    }

    //Lista de productos con bajo stock

    fun obtenerProductosStockBajo(): List<Producto> {
        return _productos.filter { it.stockActual <= it.stockMinimo }
    }


    //Agregar producto
    fun agregarProducto(nuevoProducto: Producto) {
        val buscado: Producto? = buscarProducto(nuevoProducto.codigo)

        if(buscado==null){
            _productos.add(nuevoProducto)
        }
    }

    fun actualizarProducto(productoActualizado: Producto) {
        val index = _productos.indexOfFirst { it.codigo.equals(productoActualizado.codigo, ignoreCase = true)}
        if (index != -1) {
            _productos[index] = productoActualizado
        }
    }

    fun borrarProducto(codigoProducto: String){
        /*
        version estilo Java
        val buscado: Producto?=buscarProducto(codigoProducto)
        if (buscado!=null) _productos.remove(buscado)
         */
        _productos.removeAll{it.codigo.equals(codigoProducto,ignoreCase=true)}//con sintaxis de Kotlin
    }


    //agregar CRUD de movimientos




}

