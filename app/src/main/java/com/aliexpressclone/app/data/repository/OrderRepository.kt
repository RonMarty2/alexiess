package com.aliexpressclone.app.data.repository

import com.aliexpressclone.app.data.local.dao.CartDao
import com.aliexpressclone.app.data.local.dao.OrderDao
import com.aliexpressclone.app.data.local.entity.Order
import com.aliexpressclone.app.data.local.entity.OrderItem
import com.aliexpressclone.app.data.local.entity.OrderStatus
import com.aliexpressclone.app.data.local.entity.OrderTrackingEvent
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class OrderRepository(
    private val orderDao: OrderDao,
    private val cartDao: CartDao
) {
    fun observeOrdersForUser(userId: Long): Flow<List<Order>> = orderDao.observeOrdersForUser(userId)

    fun observeAllOrders(): Flow<List<Order>> = orderDao.observeAllOrders()

    fun observeOrder(orderId: Long): Flow<Order?> = orderDao.observeOrderById(orderId)

    fun observeItems(orderId: Long): Flow<List<OrderItem>> = orderDao.observeItemsForOrder(orderId)

    fun observeTracking(orderId: Long): Flow<List<OrderTrackingEvent>> = orderDao.observeTrackingForOrder(orderId)

    suspend fun placeOrderWithItems(
        userId: Long,
        shippingName: String,
        shippingPhone: String,
        shippingAddress: String,
        items: List<OrderItem>,
        total: Double
    ): Long {
        val now = System.currentTimeMillis()
        val estimatedDelivery = now + TimeUnit.DAYS.toMillis(Random.nextLong(7, 21))
        val orderNumber = (8_000_000_000_000L + Random.nextLong(0, 999_999_999L)).toString()

        val orderId = orderDao.insertOrder(
            Order(
                userId = userId,
                orderNumber = orderNumber,
                createdAt = now,
                status = OrderStatus.A_PAGAR,
                total = total,
                shippingName = shippingName,
                shippingPhone = shippingPhone,
                shippingAddress = shippingAddress,
                estimatedDeliveryDate = estimatedDelivery
            )
        )
        orderDao.insertOrderItems(items.map { it.copy(orderId = orderId) })
        orderDao.insertTrackingEvent(
            OrderTrackingEvent(
                orderId = orderId,
                status = OrderStatus.A_PAGAR,
                date = now,
                note = "Pedido realizado. Esperando confirmación de pago."
            )
        )
        cartDao.clearForUser(userId)
        return orderId
    }

    suspend fun updateStatus(order: Order, newStatus: OrderStatus, note: String, estimatedDeliveryDate: Long?) {
        orderDao.updateOrder(order.copy(status = newStatus, estimatedDeliveryDate = estimatedDeliveryDate))
        orderDao.insertTrackingEvent(
            OrderTrackingEvent(
                orderId = order.id,
                status = newStatus,
                date = System.currentTimeMillis(),
                note = note
            )
        )
    }

}
