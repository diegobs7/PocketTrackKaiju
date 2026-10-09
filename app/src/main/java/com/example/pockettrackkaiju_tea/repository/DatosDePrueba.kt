package com.example.pockettrackkaiju_tea.repository

import com.example.pockettrackkaiju_tea.model.Movimiento
import com.example.pockettrackkaiju_tea.model.Producto
import com.example.pockettrackkaiju_tea.model.Rol
import com.example.pockettrackkaiju_tea.model.TipoMovimiento
import com.example.pockettrackkaiju_tea.model.Usuario

object DatosDePrueba {

    val vendedor=Usuario(id = "U-1", nombre = "Thomas Araya", rol = Rol.VENDEDOR)
    val admin=Usuario(id = "U-2", nombre = "Andrés Marín", rol = Rol.ADMINISTRADOR)
    val inv=Usuario(id = "U-3", nombre = "Diego Bergeret", rol = Rol.ENCARGADO_INVENTARIO)

    val usuarios: List<Usuario> = listOf(vendedor,admin,inv)

    val productos = listOf(
        Producto(
            codigo = "P-1",
            nombre = "Arroz Grado 1 1kg",
            descripcion = "Arroz de grano largo selección especial",
            categoria = "Abarrotes",
            tipo = "Alimento",
            precio = 1490,
            stockActual = 25,
            stockMinimo = 10,
            detalle = "Marca Tucapel"
        ),
        Producto(
            codigo = "P-2",
            nombre = "Aceite Vegetal 1L",
            descripcion = "Aceite vegetal libre de colesterol",
            categoria = "Abarrotes",
            tipo = "Alimento",
            precio = 2990,
            stockActual = 3,
            stockMinimo = 8,
            detalle = null
        ),
        Producto(
            codigo = "P-3",
            nombre = "Leche Entera 1L",
            descripcion = "Leche líquida entera UHT de vaca",
            categoria = "Lácteos",
            tipo = "Perecible",
            precio = 1150,
            stockActual = 15,
            stockMinimo = 10,
            detalle = "Mantener refrigerado tras abrir"
        ),
        Producto(
            codigo = "PROD-4",
            nombre = "Detergente Multiuso 3L",
            descripcion = "Detergente líquido concentrado para ropa",
            categoria = "Limpieza",
            tipo = "Insumo",
            precio = 6990,
            stockActual = 2,
            stockMinimo = 5,
            detalle = null
        ),
        Producto(
            codigo = "P-5",
            nombre = "Atún en Lomitos 160g",
            descripcion = "Lomitos de atún en agua",
            categoria = "Enlatados",
            tipo = "Alimento",
            precio = 1290,
            stockActual = 40,
            stockMinimo = 15,
            detalle = "Envasado de origen"
        ),
        Producto(
            codigo = "P-6",
            nombre = "Café Granulado 170g",
            descripcion = "Café instantáneo 100% puro tostado",
            categoria = "Abarrotes",
            tipo = "Alimento",
            precio = 4590,
            stockActual = 8,
            stockMinimo = 5,
            detalle = null
        ),
        Producto(
            codigo = "P-007",
            nombre = "Jabón Líquido 500ml",
            descripcion = "Jabón para manos con acción antibacterial",
            categoria = "Higiene",
            tipo = "Insumo",
            precio = 2190,
            stockActual = 4,
            stockMinimo = 10,
            detalle = "Aroma a lavanda"
        ),
        Producto(
            codigo = "P-008",
            nombre = "Pasta Espageti 400g",
            descripcion = "Fideos de sémola de trigo duro",
            categoria = "Abarrotes",
            tipo = "Alimento",
            precio = 890,
            stockActual = 30,
            stockMinimo = 12,
            detalle = null
        ),
        Producto(
            codigo = "P-009",
            nombre = "Harina Sin Polvos 1kg",
            descripcion = "Harina de trigo especial para repostería",
            categoria = "Abarrotes",
            tipo = "Alimento",
            precio = 1190,
            stockActual = 18,
            stockMinimo = 10,
            detalle = "Grado 000"
        ),
        Producto(
            codigo = "P-010",
            nombre = "Servilletas 200u",
            descripcion = "Servilletas de papel hoja simple",
            categoria = "Higiene",
            tipo = "Insumo",
            precio = 1590,
            stockActual = 50,
            stockMinimo = 20,
            detalle = null
        )
    )

    val movimientos = listOf(
        Movimiento(
            id = "MOV-1",
            codigoProducto = "PROD-001",
            tipo = TipoMovimiento.ENTRADA,
            cantidad = 20,
            motivo = "Recepción de mercadería de proveedor",
            fecha = "2026-10-01",
            usuario = inv
        ),
        Movimiento(
            id = "MOV-2",
            codigoProducto = "PROD-002",
            tipo = TipoMovimiento.SALIDA_VENTA,
            cantidad = 5,
            motivo = "Venta Nro. 1042",
            fecha = "2026-10-02",
            usuario = inv
        ),
        Movimiento(
            id = "MOV-3",
            codigoProducto = "PROD-004",
            tipo = TipoMovimiento.SALIDA_CONSUMO,
            cantidad = 1,
            motivo = "Uso interno en limpieza de local",
            fecha = "2026-10-03",
            usuario = admin
        ),
        Movimiento(
            id = "MOV-4",
            codigoProducto = "PROD-007",
            tipo = TipoMovimiento.AJUSTE,
            cantidad = 2,
            motivo = "Ajuste por producto dañado en góndola",
            fecha = "2026-10-04",
            usuario = vendedor
        )
    )
}










