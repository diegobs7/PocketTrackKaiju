package com.example.pockettrackkaiju_tea.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class CategoriaProducto(val displayName: String) {
    ANIME("Anime / Manga"),
    VIDEOJUEGOS("Videojuegos"),
    ASIAN_KAIJU("Asian Kaiju / Tradicional"),
    SERIES_PELICULAS("Series y Películas"),
    COMICS("Cómics"),
    OTROS("Otros")
}

enum class TipoProducto(val displayName: String) {
    POLERA("Polera Manga Corta"),
    POLERA_MANGA_LARGA("Polera Manga Larga"),
    POLERA_SIN_MANGAS("Polera Sin Mangas"),
    POLERON_CANGURO("Polerón Canguro (Capucha)"),
    POLERON_POLO("Polerón Polo (Cuello redondo)"),
    CHAQUETA("Chaqueta"),
    ACCESORIOS("Accesorios (Tazones, Stickers, etc)")
}
@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val codigo: String,
    val nombre: String,
    val descripcion: String,
    val categoria: CategoriaProducto,
    val tipo: TipoProducto,
    val precio: Int,
    val stockActual: Int,
    val stockMinimo: Int = 5,
    val detalle: String? = null
)