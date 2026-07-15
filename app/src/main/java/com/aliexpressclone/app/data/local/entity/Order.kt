package com.aliexpressclone.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val orderNumber: String,
    val createdAt: Long,
    val status: OrderStatus,
    val total: Double,
    val shippingName: String,
    val shippingPhone: String,
    val shippingAddress: String,
    val estimatedDeliveryDate: Long?,
    val trackingNumber: String? = null
)
