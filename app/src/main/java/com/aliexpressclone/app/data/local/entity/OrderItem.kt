package com.aliexpressclone.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Long,
    val productId: Long,
    val productName: String,
    val storeName: String,
    val unitPrice: Double,
    val quantity: Int,
    val placeholderEmoji: String,
    val placeholderColorHex: String,
    val imageUri: String? = null,
    val realDescription: String? = null
)
