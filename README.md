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
Administrador) desde la pantalla de registro.

## Qué puede hacer cada rol

**Comprador**
- Explorar inicio, categorías y buscador con productos de ejemplo.
- Ver el detalle de un producto y la página de la tienda que lo vende.
- Añadir productos al carrito, ajustar cantidades y simular una compra
  (dirección + "método de pago" simulado, sin cobros reales).
- Ver "Mis pedidos" filtrado por estado y el detalle de cada pedido con su
  línea de tiempo de seguimiento.

**Administrador**
- Panel con resumen (productos, compradores, pedidos, ventas simuladas).
- Crear, editar y eliminar productos (precio, precio original/descuento, stock,
  categoría, tienda, ícono y color de marcador de posición).
- Ver todos los pedidos de todos los compradores y actualizar su estado
  (A pagar → Procesando → Enviado → En tránsito → Finalizado/Cancelado),
  agregar una nota de seguimiento y ajustar la fecha estimada de entrega.

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
