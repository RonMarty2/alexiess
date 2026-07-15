package com.aliexpressclone.app.data.seed

import com.aliexpressclone.app.data.local.AppDatabase
import com.aliexpressclone.app.data.local.entity.Category
import com.aliexpressclone.app.data.local.entity.Order
import com.aliexpressclone.app.data.local.entity.OrderItem
import com.aliexpressclone.app.data.local.entity.OrderStatus
import com.aliexpressclone.app.data.local.entity.OrderTrackingEvent
import com.aliexpressclone.app.data.local.entity.Product
import com.aliexpressclone.app.data.local.entity.Role
import com.aliexpressclone.app.data.local.entity.User
import java.util.concurrent.TimeUnit

/**
 * Fills the database with demo categories, products, users and orders the first
 * time the app runs, so the clone never starts empty.
 */
class Seeder(private val db: AppDatabase) {

    suspend fun seedIfNeeded() {
        if (db.userDao().count() > 0) return

        val adminId = db.userDao().insert(
            User(
                name = "Admin Demo",
                email = "admin@demo.com",
                password = "admin123",
                role = Role.ADMIN
            )
        )
        val buyerId = db.userDao().insert(
            User(
                name = "Comprador Demo",
                email = "buyer@demo.com",
                password = "buyer123",
                role = Role.BUYER,
                phone = "+591 60395685",
                address = "Cochabamba, Valle Hermoso, Calle Grecia s/n, Cercado, Bolivia"
            )
        )
        check(adminId >= 0 && buyerId >= 0)

        val categoryIds = seedCategories()
        val products = seedProducts(categoryIds)
        seedDemoOrders(buyerId, products)
    }

    private suspend fun seedCategories(): Map<String, Long> {
        val categories = listOf(
            Category(name = "Electrónica", emoji = "📱", colorHex = "#B3E5FC"),
            Category(name = "Moda", emoji = "👕", colorHex = "#F8BBD0"),
            Category(name = "Hogar", emoji = "🏠", colorHex = "#C8E6C9"),
            Category(name = "Belleza", emoji = "💄", colorHex = "#FFCCBC"),
            Category(name = "Deportes", emoji = "⚽", colorHex = "#DCEDC8"),
            Category(name = "Juguetes", emoji = "🧸", colorHex = "#FFF9C4"),
            Category(name = "Mascotas", emoji = "🐾", colorHex = "#D7CCC8"),
            Category(name = "Accesorios", emoji = "🎒", colorHex = "#D1C4E9")
        )
        db.categoryDao().insertAll(categories)
        val saved = db.categoryDao().getAll()
        return saved.associate { it.name to it.id }
    }

    private suspend fun seedProducts(categoryIds: Map<String, Long>): List<Product> {
        fun cat(name: String) = categoryIds.getValue(name)

        val products = listOf(
            // Electrónica
            Product(name = "Cable USB-C UNO PD100W Carga Rápida 2m", description = "Cable de carga rápida 100W con pantalla digital, compatible con laptops y celulares.", price = 10.26, originalPrice = 10.69, stock = 150, categoryId = cat("Electrónica"), storeName = "NexTech Official Store", rating = 4.9f, soldCount = 20500, placeholderEmoji = "🔌", placeholderColorHex = "#B3E5FC"),
            Product(name = "Audífonos Inalámbricos Bluetooth 5.3 Cancelación de Ruido", description = "Sonido envolvente, hasta 30 horas de batería, resistentes al agua IPX5.", price = 19.79, originalPrice = 17.79, stock = 80, categoryId = cat("Electrónica"), storeName = "NexTech Official Store", rating = 4.8f, soldCount = 890, placeholderEmoji = "🎧", placeholderColorHex = "#B3E5FC"),
            Product(name = "Hub USB-C 9 en 1 HDMI 4K 60Hz PD100W", description = "Expande tu laptop con HDMI, lectores SD/TF, USB 3.0 y carga rápida.", price = 51.39, originalPrice = 122.93, stock = 25, categoryId = cat("Electrónica"), storeName = "Yottamaster Top Direct", rating = 4.7f, soldCount = 340, placeholderEmoji = "🖥️", placeholderColorHex = "#B3E5FC"),
            Product(name = "Adaptador de Audio 3.5mm Macho a Hembra 2 Piezas", description = "Adaptador dorado de 2.5mm a 3.5mm, para auriculares y micrófonos.", price = 5.83, originalPrice = 6.90, stock = 200, categoryId = cat("Electrónica"), storeName = "Vention Flagship Store", rating = 5.0f, soldCount = 1200, placeholderEmoji = "🎵", placeholderColorHex = "#B3E5FC"),
            Product(name = "Power Bank Magnético 10000mAh Carga Inalámbrica", description = "Batería portátil compatible con MagSafe, carga rápida 20W.", price = 24.99, originalPrice = 34.99, stock = 60, categoryId = cat("Electrónica"), storeName = "NexTech Official Store", rating = 4.6f, soldCount = 2300, placeholderEmoji = "🔋", placeholderColorHex = "#B3E5FC"),
            Product(name = "Memoria RAM DDR4 16GB 3200MHz Kit 2x8GB", description = "Módulos de memoria con disipador RGB para PC de escritorio.", price = 66.48, originalPrice = 79.90, stock = 15, categoryId = cat("Electrónica"), storeName = "PC Parts Direct", rating = 4.9f, soldCount = 540, placeholderEmoji = "💾", placeholderColorHex = "#B3E5FC"),
            Product(name = "Mini PC 16 Núcleos Windows 11 32GB RAM 1TB SSD", description = "Computadora compacta de alto rendimiento con WiFi 6 y Bluetooth 5.2.", price = 312.13, originalPrice = 337.13, stock = 10, categoryId = cat("Electrónica"), storeName = "PC Parts Direct", rating = 4.7f, soldCount = 95, placeholderEmoji = "💻", placeholderColorHex = "#B3E5FC"),
            Product(name = "Smartwatch Deportivo Pantalla AMOLED", description = "Monitoreo de ritmo cardíaco, GPS integrado y resistencia al agua 5ATM.", price = 28.50, originalPrice = 45.00, stock = 45, categoryId = cat("Electrónica"), storeName = "NexTech Official Store", rating = 4.5f, soldCount = 3100, placeholderEmoji = "⌚", placeholderColorHex = "#B3E5FC"),

            // Moda
            Product(name = "Chamarra Impermeable Unisex Rompevientos", description = "Ligera, plegable y resistente al agua, ideal para exteriores.", price = 22.90, originalPrice = 39.90, stock = 70, categoryId = cat("Moda"), storeName = "Urban Style Co", rating = 4.6f, soldCount = 1800, placeholderEmoji = "🧥", placeholderColorHex = "#F8BBD0"),
            Product(name = "Zapatillas Deportivas para Correr Ultra Ligeras", description = "Suela de espuma con amortiguación y malla transpirable.", price = 31.99, originalPrice = 55.00, stock = 55, categoryId = cat("Moda"), storeName = "Urban Style Co", rating = 4.7f, soldCount = 4200, placeholderEmoji = "👟", placeholderColorHex = "#F8BBD0"),
            Product(name = "Reloj Análogo Acero Inoxidable Vintage", description = "Correa metálica ajustable, resistente al agua, estilo retro.", price = 11.99, originalPrice = 25.35, stock = 90, categoryId = cat("Moda"), storeName = "Skmei Official Store", rating = 4.7f, soldCount = 520, placeholderEmoji = "⌚", placeholderColorHex = "#F8BBD0"),
            Product(name = "Mochila Antirrobo con Puerto USB", description = "Compartimento acolchado para laptop de 15.6\", tela impermeable.", price = 18.75, originalPrice = 29.90, stock = 65, categoryId = cat("Moda"), storeName = "Urban Style Co", rating = 4.8f, soldCount = 2650, placeholderEmoji = "🎒", placeholderColorHex = "#F8BBD0"),

            // Hogar
            Product(name = "Organizador de Cocina Giratorio 360°", description = "Bandeja giratoria de plástico resistente para especias y condimentos.", price = 9.49, originalPrice = 14.99, stock = 120, categoryId = cat("Hogar"), storeName = "Home Comfort Store", rating = 4.5f, soldCount = 980, placeholderEmoji = "🍽️", placeholderColorHex = "#C8E6C9"),
            Product(name = "Set de Sábanas Microfibra 4 Piezas", description = "Suaves, hipoalergénicas y de secado rápido, varias tallas disponibles.", price = 16.20, originalPrice = 27.00, stock = 40, categoryId = cat("Hogar"), storeName = "Home Comfort Store", rating = 4.6f, soldCount = 1340, placeholderEmoji = "🛏️", placeholderColorHex = "#C8E6C9"),
            Product(name = "Lámpara LED de Escritorio Recargable", description = "3 modos de luz, brazo flexible y batería de larga duración.", price = 12.49, originalPrice = 19.99, stock = 75, categoryId = cat("Hogar"), storeName = "Home Comfort Store", rating = 4.7f, soldCount = 760, placeholderEmoji = "💡", placeholderColorHex = "#C8E6C9"),
            Product(name = "Set de Ollas Antiadherentes 6 Piezas", description = "Aptas para todo tipo de estufas, mangos ergonómicos resistentes al calor.", price = 45.00, originalPrice = 68.00, stock = 20, categoryId = cat("Hogar"), storeName = "Home Comfort Store", rating = 4.8f, soldCount = 410, placeholderEmoji = "🍳", placeholderColorHex = "#C8E6C9"),

            // Belleza
            Product(name = "Set de Brochas de Maquillaje Profesional 12 Piezas", description = "Cerdas suaves sintéticas, incluye estuche de viaje.", price = 7.99, originalPrice = 15.99, stock = 130, categoryId = cat("Belleza"), storeName = "GlamBeauty Store", rating = 4.6f, soldCount = 2200, placeholderEmoji = "💄", placeholderColorHex = "#FFCCBC"),
            Product(name = "Plancha de Cabello Cerámica Profesional", description = "Calentamiento rápido, temperatura ajustable hasta 230°C.", price = 21.50, originalPrice = 35.00, stock = 35, categoryId = cat("Belleza"), storeName = "GlamBeauty Store", rating = 4.5f, soldCount = 1050, placeholderEmoji = "💇", placeholderColorHex = "#FFCCBC"),
            Product(name = "Crema Facial Hidratante con Ácido Hialurónico", description = "Fórmula ligera de absorción rápida para todo tipo de piel.", price = 9.90, originalPrice = 16.90, stock = 100, categoryId = cat("Belleza"), storeName = "GlamBeauty Store", rating = 4.7f, soldCount = 3300, placeholderEmoji = "🧴", placeholderColorHex = "#FFCCBC"),
            Product(name = "Espejo LED de Maquillaje con Aumento", description = "Iluminación regulable y espejo de aumento 10x desmontable.", price = 14.30, originalPrice = 22.00, stock = 50, categoryId = cat("Belleza"), storeName = "GlamBeauty Store", rating = 4.6f, soldCount = 690, placeholderEmoji = "🪞", placeholderColorHex = "#FFCCBC"),

            // Deportes
            Product(name = "Bandas de Resistencia Set 5 Niveles", description = "Ideales para entrenamiento funcional, incluye bolsa de transporte.", price = 8.49, originalPrice = 14.00, stock = 140, categoryId = cat("Deportes"), storeName = "SportFit Store", rating = 4.7f, soldCount = 2900, placeholderEmoji = "🏋️", placeholderColorHex = "#DCEDC8"),
            Product(name = "Botella Térmica de Acero Inoxidable 1L", description = "Mantiene la temperatura hasta 24 horas, libre de BPA.", price = 10.99, originalPrice = 18.00, stock = 95, categoryId = cat("Deportes"), storeName = "SportFit Store", rating = 4.8f, soldCount = 1750, placeholderEmoji = "🚰", placeholderColorHex = "#DCEDC8"),
            Product(name = "Balón de Fútbol Profesional N°5", description = "Costura reforzada, superficie texturizada para mejor control.", price = 17.90, originalPrice = 26.00, stock = 60, categoryId = cat("Deportes"), storeName = "SportFit Store", rating = 4.6f, soldCount = 830, placeholderEmoji = "⚽", placeholderColorHex = "#DCEDC8"),
            Product(name = "Guantes de Entrenamiento con Muñequera", description = "Acolchado de gel, transpirables, ajuste con velcro.", price = 12.75, originalPrice = 20.00, stock = 70, categoryId = cat("Deportes"), storeName = "SportFit Store", rating = 4.5f, soldCount = 1120, placeholderEmoji = "🥊", placeholderColorHex = "#DCEDC8"),

            // Juguetes
            Product(name = "Set de Bloques de Construcción 500 Piezas", description = "Compatible con las principales marcas, estimula la creatividad.", price = 15.99, originalPrice = 24.99, stock = 55, categoryId = cat("Juguetes"), storeName = "ToyWorld Store", rating = 4.8f, soldCount = 1980, placeholderEmoji = "🧱", placeholderColorHex = "#FFF9C4"),
            Product(name = "Dron Plegable con Cámara HD para Principiantes", description = "Modo de vuelo estable, control por app, batería de 20 minutos.", price = 34.90, originalPrice = 55.00, stock = 30, categoryId = cat("Juguetes"), storeName = "ToyWorld Store", rating = 4.6f, soldCount = 640, placeholderEmoji = "🚁", placeholderColorHex = "#FFF9C4"),
            Product(name = "Peluche de Oso Gigante 80cm", description = "Suave, hipoalergénico, ideal como regalo.", price = 19.50, originalPrice = 32.00, stock = 40, categoryId = cat("Juguetes"), storeName = "ToyWorld Store", rating = 4.9f, soldCount = 1290, placeholderEmoji = "🧸", placeholderColorHex = "#FFF9C4"),

            // Mascotas
            Product(name = "Cama Ortopédica para Mascotas Talla M", description = "Espuma viscoelástica, funda desmontable y lavable.", price = 23.00, originalPrice = 36.00, stock = 35, categoryId = cat("Mascotas"), storeName = "PetCare Store", rating = 4.7f, soldCount = 720, placeholderEmoji = "🐶", placeholderColorHex = "#D7CCC8"),
            Product(name = "Dispensador Automático de Comida para Mascotas", description = "Programable hasta 4 horarios, compartimento para 4L de alimento.", price = 29.99, originalPrice = 42.00, stock = 25, categoryId = cat("Mascotas"), storeName = "PetCare Store", rating = 4.6f, soldCount = 480, placeholderEmoji = "🍖", placeholderColorHex = "#D7CCC8"),
            Product(name = "Rascador para Gatos Torre 3 Niveles", description = "Estructura estable forrada en sisal, incluye juguete colgante.", price = 27.50, originalPrice = 40.00, stock = 20, categoryId = cat("Mascotas"), storeName = "PetCare Store", rating = 4.8f, soldCount = 350, placeholderEmoji = "🐱", placeholderColorHex = "#D7CCC8"),

            // Accesorios
            Product(name = "Funda Protectora para Smartphone Anti-golpes", description = "Bordes reforzados con silicona, compatible con carga inalámbrica.", price = 6.49, originalPrice = 11.00, stock = 200, categoryId = cat("Accesorios"), storeName = "NexTech Official Store", rating = 4.7f, soldCount = 5200, placeholderEmoji = "📱", placeholderColorHex = "#D1C4E9"),
            Product(name = "Lentes de Sol Polarizados Unisex", description = "Protección UV400, montura ligera de acetato.", price = 9.99, originalPrice = 17.00, stock = 85, categoryId = cat("Accesorios"), storeName = "Urban Style Co", rating = 4.5f, soldCount = 1460, placeholderEmoji = "🕶️", placeholderColorHex = "#D1C4E9"),
            Product(name = "Correa para Reloj Inteligente Silicona", description = "Compatible con múltiples modelos, ajuste cómodo y transpirable.", price = 4.99, originalPrice = 9.00, stock = 250, categoryId = cat("Accesorios"), storeName = "NexTech Official Store", rating = 4.6f, soldCount = 3800, placeholderEmoji = "⌚", placeholderColorHex = "#D1C4E9"),
            Product(name = "Organizador de Cables de Viaje", description = "Múltiples compartimentos para cargadores, cables y accesorios.", price = 8.90, originalPrice = 14.90, stock = 110, categoryId = cat("Accesorios"), storeName = "NexTech Official Store", rating = 4.7f, soldCount = 990, placeholderEmoji = "🎒", placeholderColorHex = "#D1C4E9")
        )

        db.productDao().insertAll(products)
        return db.productDao().getAllSnapshot()
    }

    private suspend fun seedDemoOrders(buyerId: Long, products: List<Product>) {
        if (products.size < 6) return
        val day = TimeUnit.DAYS.toMillis(1)
        val now = System.currentTimeMillis()

        // Order 1: fully delivered a while ago
        createDemoOrder(
            buyerId = buyerId,
            product = products[3],
            quantity = 1,
            createdAt = now - day * 45,
            history = listOf(
                OrderStatus.A_PAGAR to "Pedido realizado.",
                OrderStatus.PROCESANDO to "El vendedor está preparando tu paquete.",
                OrderStatus.ENVIADO to "Paquete entregado al transportista.",
                OrderStatus.EN_TRANSITO to "El paquete está en camino a tu ciudad.",
                OrderStatus.FINALIZADO to "Entregado. ¡Gracias por tu compra!"
            ),
            daySpacing = 6
        )

        // Order 2: in transit right now
        createDemoOrder(
            buyerId = buyerId,
            product = products[9],
            quantity = 2,
            createdAt = now - day * 5,
            history = listOf(
                OrderStatus.A_PAGAR to "Pedido realizado.",
                OrderStatus.PROCESANDO to "El vendedor está preparando tu paquete.",
                OrderStatus.ENVIADO to "Paquete entregado al transportista.",
                OrderStatus.EN_TRANSITO to "El paquete está en camino a tu ciudad."
            ),
            daySpacing = 1
        )

        // Order 3: just placed, waiting for payment confirmation
        createDemoOrder(
            buyerId = buyerId,
            product = products[20],
            quantity = 1,
            createdAt = now - TimeUnit.HOURS.toMillis(3),
            history = listOf(
                OrderStatus.A_PAGAR to "Pedido realizado. Esperando confirmación de pago."
            ),
            daySpacing = 0
        )
    }

    private suspend fun createDemoOrder(
        buyerId: Long,
        product: Product,
        quantity: Int,
        createdAt: Long,
        history: List<Pair<OrderStatus, String>>,
        daySpacing: Long
    ) {
        val day = TimeUnit.DAYS.toMillis(1)
        val total = product.price * quantity
        val lastStatus = history.last().first
        val estimatedDelivery = if (lastStatus == OrderStatus.FINALIZADO) null else createdAt + day * 12

        val orderId = db.orderDao().insertOrder(
            Order(
                userId = buyerId,
                orderNumber = (8_100_000_000_000L + product.id * 37 + createdAt % 1000).toString(),
                createdAt = createdAt,
                status = lastStatus,
                total = total,
                shippingName = "Comprador Demo",
                shippingPhone = "+591 60395685",
                shippingAddress = "Cochabamba, Valle Hermoso, Calle Grecia s/n, Cercado, Bolivia",
                estimatedDeliveryDate = estimatedDelivery
            )
        )
        db.orderDao().insertOrderItems(
            listOf(
                OrderItem(
                    orderId = orderId,
                    productId = product.id,
                    productName = product.name,
                    storeName = product.storeName,
                    unitPrice = product.price,
                    quantity = quantity,
                    placeholderEmoji = product.placeholderEmoji,
                    placeholderColorHex = product.placeholderColorHex
                )
            )
        )
        history.forEachIndexed { index, (status, note) ->
            db.orderDao().insertTrackingEvent(
                OrderTrackingEvent(
                    orderId = orderId,
                    status = status,
                    date = createdAt + day * daySpacing * index,
                    note = note
                )
            )
        }
    }
}
