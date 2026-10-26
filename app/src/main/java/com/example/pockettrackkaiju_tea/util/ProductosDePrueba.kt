package com.example.pockettrackkaiju_tea.util


import com.example.pockettrackkaiju_tea.model.CategoriaProducto
import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.model.TipoProducto

object ProductosDePrueba {

    val lista = listOf(
        Producto(
            codigo = "KAI-001",
            nombre = "Polera Slam Dunk Shohoku",
            descripcion = "Polera 100% algodón con diseño de Hanamichi Sakuragi",
            categoria = CategoriaProducto.ANIME,
            tipo = TipoProducto.POLERA,
            precio = 16990,
            stockActual = 25,
            stockMinimo = 5,
            detalle = "Estampado en serigrafía tradicional"
        ),
        Producto(
            codigo = "KAI-002",
            nombre = "Polerón Canguro Zelda Tears of the Kingdom",
            descripcion = "Polerón con capucha y bolsillo frontal con logo de Zelda",
            categoria = CategoriaProducto.VIDEOJUEGOS,
            tipo = TipoProducto.POLERON_CANGURO,
            precio = 32990,
            stockActual = 10,
            stockMinimo = 5
        ),
        Producto(
            codigo = "KAI-003",
            nombre = "Polera Berserk Guts Armor",
            descripcion = "Polera negra manga corta con panel del manga de Berserk",
            categoria = CategoriaProducto.ANIME,
            tipo = TipoProducto.POLERA,
            precio = 16990,
            stockActual = 12,
            stockMinimo = 5,
            detalle = "Corte oversize"
        ),
        Producto(
            codigo = "KAI-004",
            nombre = "Polera Manga Larga Jujutsu Kaisen",
            descripcion = "Diseño de Satoru Gojo en técnica de semitono",
            categoria = CategoriaProducto.ANIME,
            tipo = TipoProducto.POLERA_MANGA_LARGA,
            precio = 19990,
            stockActual = 10,
            stockMinimo = 5
        ),
        Producto(
            codigo = "KAI-005",
            nombre = "Polera Chrono Trigger Team",
            descripcion = "Polera clásica con arte original del videojuego de SNES",
            categoria = CategoriaProducto.VIDEOJUEGOS,
            tipo = TipoProducto.POLERA,
            precio = 15990,
            stockActual = 20,
            stockMinimo = 5,
            detalle = "Edición limitada"
        ),
        Producto(
            codigo = "KAI-006",
            nombre = "Polerón Polo Hunter x Hunter",
            descripcion = "Polerón cuello redondo con bordado de la licencia de Cazador",
            categoria = CategoriaProducto.ANIME,
            tipo = TipoProducto.POLERON_POLO,
            precio = 28990,
            stockActual = 15,
            stockMinimo = 5
        ),
        Producto(
            codigo = "KAI-007",
            nombre = "Polera Asian Kaiju Oni Mask",
            descripcion = "Diseño original de máscara Oni estilo japonés",
            categoria = CategoriaProducto.ASIAN_KAIJU,
            tipo = TipoProducto.POLERA,
            precio = 17990,
            stockActual = 30,
            stockMinimo = 5,
            detalle = "Diseño exclusivo de la marca"
        ),
        Producto(
            codigo = "KAI-008",
            nombre = "Polera Sin Mangas Evangelion",
            descripcion = "Polera sin mangas ideal para verano, diseño EVA-01",
            categoria = CategoriaProducto.ANIME,
            tipo = TipoProducto.POLERA_SIN_MANGAS,
            precio = 14990,
            stockActual = 10,
            stockMinimo = 5
        ),
        Producto(
            codigo = "KAI-009",
            nombre = "Pack Stickers Pokémon Kanto",
            descripcion = "Set de 10 stickers de vinilo resistentes al agua",
            categoria = CategoriaProducto.VIDEOJUEGOS,
            tipo = TipoProducto.ACCESORIOS,
            precio = 4990,
            stockActual = 50,
            stockMinimo = 5,
            detalle = "Incluye los iniciales de Kanto"
        ),
        Producto(
            codigo = "KAI-010",
            nombre = "Polerón Canguro Akira",
            descripcion = "Polerón rojo icónico con diseño de la píldora de Akira",
            categoria = CategoriaProducto.ANIME,
            tipo = TipoProducto.POLERON_CANGURO,
            precio = 34990,
            stockActual = 10,
            stockMinimo = 5,
            detalle = "Algodón peinado premium"
        )
    )
}