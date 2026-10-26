# PocketTrackKaiju — Grupo TDA

**Integrantes:** Thomas Araya, Diego Bergeret, Andres Marin

**Caso:** Gestión y trazabilidad de inventario para Kaiju. La tienda llevaba el control de stock en registros manuales y planillas, lo que provocaba diferencias entre el stock registrado y el real, olvidos de entradas o salidas y poca visibilidad de los productos por reponer. PocketTrackKaiju es una app Android que permite consultar productos, registrar movimientos de inventario y ventas, y avisar cuando un producto llega a su stock mínimo, con acceso según el rol de cada persona.

**Arquitectura:** Kotlin + Jetpack Compose (Material 3), patrón MVVM con Room para la persistencia local.

## Diseño y planificación
- Sketch, wireframe, mockup y demás material de diseño del equipo: [Carpeta de Drive](https://drive.google.com/drive/folders/1mIGXUw-aqeLa104GZ0dn6G4jYbKgph-0?usp=sharing)
- Planificación en Trello: [Aplicacion Movil Para Kaiju](https://trello.com/b/I1EDK3JD/aplicacion-movil-para-kaiju)

## Funcionalidades implementadas
- **Inicio de sesión** con correo y contraseña, con opción "Recordarme" (se guarda solo el id del usuario en SharedPreferences). **Registro** de usuarios nuevos, que quedan con rol Vendedor.
- **Roles y permisos:** el menú y las acciones cambian según el rol.
- **Catálogo** de productos con búsqueda y filtro por categoría, y **detalle del producto** con foto, stock, indicador de stock bajo e historial de movimientos.
- **Gestión de productos** (administrador): agregar, editar y eliminar con confirmación. No se puede eliminar un producto que ya tiene movimientos.
- **Movimientos de inventario:** entrada, salida por venta, salida por consumo y ajuste. Actualizan el stock y no permiten sacar más unidades de las que hay. Cada movimiento queda con fecha y responsable.
- **Ventas** (administrador): productos más y menos vendidos y categorías más vendidas.
- **Alertas de stock bajo:** pestaña con los productos que están en su stock mínimo o por debajo.
- **Gestión de usuarios** (administrador): listar, cambiar rol y eliminar.
- **Formularios validados** (login, registro, producto y movimiento) con mensajes de error por campo. Las reglas viven en los ViewModels, no en las pantallas.
- **Animaciones** de retroalimentación en los formularios y splash de inicio.
- **Pruebas unitarias** de los ViewModels (carpeta `app/src/test`).

### Permisos por rol
| Rol | Qué puede hacer |
|---|---|
| Administrador | Todo: productos, todos los tipos de movimiento, alertas, ventas y usuarios. |
| Encargado de inventario | Registrar entradas, salidas por consumo y ajustes; ver alertas. |
| Vendedor | Consultar el catálogo y registrar salidas por venta. |

## Recursos nativos
| Recurso | Permiso y tipo | RF / necesidad del caso que cumple | Qué pasa si el usuario dice que no |
|---|---|---|---|
| Cámara (CameraX + ML Kit) | `CAMERA`, permiso peligroso: se pide en tiempo de ejecución al abrir el escáner o la foto | Leer códigos de barras o QR para registrar movimientos y productos más rápido, y tomar la foto del producto | Se explica para qué se usa el permiso y se ofrece abrir los ajustes. La app sigue funcionando: los códigos se pueden escribir a mano. |
| Notificaciones | `POST_NOTIFICATIONS`, permiso peligroso desde Android 13: se pide en tiempo de ejecución. Antes de Android 13 no se pide. | Avisar cuando un producto llega a su stock mínimo | No se envían avisos, pero los productos con stock bajo se siguen viendo en la pestaña Alertas. |

## Cómo ejecutar el proyecto
**Requisitos:** Android Studio estable reciente (compatible con Android Gradle Plugin 9.x) y un celular Android 7.0 o superior (API 24+) o un emulador. La cámara se prueba mejor en un celular real.

1. Clonar el repositorio: `git clone https://github.com/diegobs7/PocketTrackKaiju.git`
2. Abrir la carpeta en Android Studio y esperar el *Gradle Sync*.
3. Conectar el celular (con depuración USB) o iniciar un emulador, y presionar **Run**.
4. Si ya había una versión anterior instalada en el dispositivo, desinstalarla primero, porque la base de datos cambió.

**Usuarios de prueba** (datos ficticios, se crean la primera vez que se abre la app; clave `1234`):

| Correo | Rol |
|---|---|
| `andres@kaiju.cl` | Administrador |
| `diego@kaiju.cl` | Encargado de inventario |
| `thomas@kaiju.cl` | Vendedor |

El catálogo parte vacío: entrar como administrador y crear productos con el botón **+**.

**Pruebas unitarias:** `./gradlew testDebugUnitTest` (o botón derecho sobre `app/src/test` y *Run Tests*).

## Estructura del proyecto (`app/src/main/java/com/example/pockettrackkaiju_tea`)
- `model/`: entidades de Room (Producto, Usuario, Movimiento), enums y reglas de permisos por rol.
- `database/`: base de datos Room y los DAOs.
- `repository/`: repositorios, los únicos que hablan con los DAOs.
- `viewmodel/`: ViewModels con el estado (StateFlow) y las validaciones, más la fábrica que les entrega los repositorios.
- `ui/`: pantallas, componentes reutilizables y tema (colores y tipografía).
- `navigation/`: rutas y NavHost.
- `util/`: sesión guardada, notificaciones, manejo de fotos y cálculo de ventas.

## Créditos
- Tipografía **Unbounded** (solo en el encabezado), © The Unbounded Project Authors, licencia SIL Open Font License 1.1. Ver `LICENCIA_Unbounded_OFL.txt`.
- Logo de Kaiju Serigrafía, usado con fines académicos.
