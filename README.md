# AliClone (Demo académica)

Clon no funcional de una tienda tipo AliExpress, hecho como app **Android nativa**
(Kotlin + Jetpack Compose) para un trabajo de Ingeniería de Sistemas.

No hay pasarelas de pago reales, ni servidores, ni envíos reales: todo el catálogo,
los pedidos y el seguimiento de envíos viven en una base de datos local (Room/SQLite)
que se puede editar desde el rol de **Administrador**.

## Stack

- Kotlin + Jetpack Compose (Material 3)
- Arquitectura MVVM (ViewModel + StateFlow)
- Room (SQLite local) para persistencia
- DataStore Preferences para la sesión (usuario logueado)
- Navigation Compose, con grafos separados para Comprador y Administrador

## Cómo abrir el proyecto

1. Abre la carpeta raíz del repositorio con **Android Studio** (Koala/2024.1 o más
   reciente recomendado).
2. Deja que Gradle sincronice (se descargan las dependencias de Compose, Room, etc.).
3. Ejecuta el módulo `app` en un emulador o dispositivo con Android 8.0 (API 26) o
   superior.

No se necesita configurar ningún backend: la primera vez que se abre la app se
crea la base de datos y se llena automáticamente con datos de ejemplo (categorías,
~34 productos, un usuario administrador y un usuario comprador con pedidos ya
generados).

## Cuentas de prueba

| Rol           | Correo           | Contraseña |
|---------------|------------------|------------|
| Administrador | admin@demo.com   | admin123   |
| Comprador     | buyer@demo.com   | buyer123   |

También puedes registrar cuentas nuevas eligiendo el rol (Comprador o
Administrador) desde la pantalla de registro, o usar los botones de acceso
directo "Entrar como Administrador" / "Entrar como Comprador" en el login.

## Qué puede hacer cada rol

**Comprador**
- Explorar inicio, categorías y buscador con productos de ejemplo.
- Ver el detalle de un producto y la página de la tienda que lo vende.
- Añadir productos al carrito, ajustar cantidades y simular una compra
  (dirección + "método de pago" simulado, sin cobros reales).
- Ver "Mis pedidos" filtrado por estado y el detalle de cada pedido con su
  línea de tiempo de seguimiento. Si el administrador ya le puso una foto y
  descripción reales al artículo, las ve ahí también.

**Administrador**
- Panel con resumen (productos, vendedores, compradores, pedidos, ventas simuladas).
- Crear y administrar **vendedores** (nombre, descripción, logo real elegido
  desde la galería del celular).
- Crear, editar y eliminar productos: precio, precio original/descuento, stock,
  categoría, vendedor asignado, y una **foto real** elegida desde la galería
  (si no se elige foto, se muestra un ícono de marcador de posición).
- Ver todos los pedidos de todos los compradores y:
  - Actualizar su estado (A pagar → Procesando → Enviado → En tránsito →
    Finalizado/Cancelado) con nota de seguimiento y fecha estimada de entrega.
  - Ponerle a cada artículo comprado una **foto real** y una **descripción real**
    de lo que efectivamente se envió.
  - Cambiar la fecha de compra del pedido (o dejarla igual).
- **Reiniciar pedidos y carritos de prueba** desde el panel: borra únicamente
  datos transaccionales (pedidos, seguimiento, carritos) y restaura el stock
  que esos pedidos habían reservado. Nunca toca productos, vendedores ni fotos,
  así que se puede demostrar la app varias veces sin perder el catálogo armado.
- **Exportar catálogo**: genera un archivo JSON con vendedores, categorías,
  productos y sus fotos (en base64) para guardarlo como respaldo permanente
  fuera del celular.

Las fotos se eligen con el selector de imágenes nativo de Android (Photo
Picker, sin pedir permisos) y la app guarda su propia copia interna, así que
siguen funcionando aunque borres la foto original de la galería.

Al comprar, el stock del producto baja automáticamente; y cada comprador
puede calificar (estrellas + comentario) los productos de un pedido ya
Finalizado, visibles luego en el detalle de cada producto.

## Estructura del código

```
app/src/main/java/com/aliexpressclone/app/
  data/           entidades Room, DAOs, repositorios, semilla de datos, sesión
  ui/auth/        login y registro
  ui/buyer/       pantallas del comprador
  ui/admin/       pantallas del administrador
  ui/components/  piezas reutilizables (tarjeta de producto, badges, etc.)
  ui/navigation/  grafo de navegación raíz y rutas
```
