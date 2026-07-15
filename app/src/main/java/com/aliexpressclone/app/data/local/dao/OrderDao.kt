package com.aliexpressclone.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aliexpressclone.app.data.local.entity.Order
import com.aliexpressclone.app.data.local.entity.OrderItem
import com.aliexpressclone.app.data.local.entity.OrderTrackingEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: Order): Long

    @Insert
    suspend fun insertOrderItems(items: List<OrderItem>)

    @Insert
    suspend fun insertTrackingEvent(event: OrderTrackingEvent)

    @Update
    suspend fun updateOrder(order: Order)

    @Update
    suspend fun updateOrderItem(item: OrderItem)

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeOrdersForUser(userId: Long): Flow<List<Order>>

    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun observeAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    fun observeOrderById(orderId: Long): Flow<Order?>

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun observeItemsForOrder(orderId: Long): Flow<List<OrderItem>>

    @Query("SELECT * FROM order_tracking WHERE orderId = :orderId ORDER BY date ASC")
    fun observeTrackingForOrder(orderId: Long): Flow<List<OrderTrackingEvent>>

    @Query("DELETE FROM order_tracking")
    suspend fun deleteAllTracking()

    @Query("DELETE FROM order_items")
    suspend fun deleteAllOrderItems()

    @Query("DELETE FROM orders")
    suspend fun deleteAllOrders()
}
