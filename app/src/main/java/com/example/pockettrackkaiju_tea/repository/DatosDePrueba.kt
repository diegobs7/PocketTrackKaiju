package com.example.pockettrackkaiju_tea.repository

import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.model.Usuario

class DatosDePrueba{

    fun obtenerUsuarios(): List<Usuario>{
        return listOf(
            Usuario(
                id = "U-1",
                nombre = "Thomas",
                rol = Rol.VENDEDOR
            ),
            Usuario(
                id= "U-2",
                nombre = "Andrés",
                rol = Rol.ADMINISTRADOR
            ),
            Usuario(
                id="U-3",
                nombre = "Diego",
                rol = Rol.ENCARGADO_INVENTARIO
            )
        )
    }




    val productos: List<Producto> = listOf(
        Producto(
            codigo = "TEA-001",
            nombre = "Té Dragón de Fuego",
            descripcion = "Té rojo especiado en hebras 100g",
            categoria = "Té en Hebras",
            tipo = "Insumo",
            precio = 6500,
            stockActual = 2,
            stockMinimo = 5,
            detalle = "Lote 2026-F"
        ),
        Producto(
            codigo = "TEA-002",
            nombre = "Infusión Estelar Menta",
            descripcion = "Mezcla herbal digestiva de menta y manzanilla",
            categoria = "Infusiones",
            tipo = "Insumo",
            precio = 4200,
            stockActual = 15,
            stockMinimo = 5
        ),
        Producto(
            codigo = "TEA-003",
            nombre = "Matcha Místico Imperial",
            descripcion = "Té verde matcha puro en polvo 50g",
            categoria = "Té en Polvo",
            tipo = "Insumo",
            precio = 12900,
            stockActual = 1,
            stockMinimo = 4,
            detalle = "Grado Ceremonial"
        ),
        Producto(
            codigo = "TEA-004",
            nombre = "Té Oolong Niebla Azul",
            descripcion = "Té azul artesanal semifermentado 80g",
            categoria = "Té en Hebras",
            tipo = "Insumo",
            precio = 8900,
            stockActual = 20,
            stockMinimo = 6
        ),
        Producto(
            codigo = "TEA-005",
            nombre = "Té Blanco Perlado",
            descripcion = "Brotes selectos de té blanco 50g",
            categoria = "Té en Hebras",
            tipo = "Insumo",
            precio = 10500,
            stockActual = 8,
            stockMinimo = 3
        ),
        Producto(
            codigo = "ACC-001",
            nombre = "Tetera Oriental Hierro",
            descripcion = "Tetera de hierro fundido negra 800ml",
            categoria = "Accesorios",
            tipo = "Producto Final",
            precio = 22900,
            stockActual = 4,
            stockMinimo = 2,
            detalle = "Incluye filtro de acero"
        ),
        Producto(
            codigo = "ACC-002",
            nombre = "Taza Térmica Mística",
            descripcion = "Taza de cerámica con tapa 350ml",
            categoria = "Accesorios",
            tipo = "Producto Final",
            precio = 7900,
            stockActual = 2,
            stockMinimo = 5
        ),
        Producto(
            codigo = "ACC-003",
            nombre = "Infusor Pinza Inoxidable",
            descripcion = "Filtro tipo pinza para tazas",
            categoria = "Accesorios",
            tipo = "Producto Final",
            precio = 3200,
            stockActual = 30,
            stockMinimo = 10
        ),
        Producto(
            codigo = "INS-001",
            nombre = "Filtros de Papel Ecológicos",
            descripcion = "Caja de 100 bolsas desechables para té",
            categoria = "Insumos",
            tipo = "Consumible",
            precio = 2500,
            stockActual = 50,
            stockMinimo = 15
        ),
        Producto(
            codigo = "TEA-006",
            nombre = "Chai Especias Ancestrales",
            descripcion = "Té negro con canela, clavo y jengibre 100g",
            categoria = "Té en Hebras",
            tipo = "Insumo",
            precio = 5800,
            stockActual = 12,
            stockMinimo = 4
        )
    )


}










