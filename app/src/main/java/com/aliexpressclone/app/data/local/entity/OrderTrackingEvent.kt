package com.aliexpressclone.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_tracking")
data class OrderTrackingEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Long,
    val status: OrderStatus,
    val date: Long,
    val note: String
)
