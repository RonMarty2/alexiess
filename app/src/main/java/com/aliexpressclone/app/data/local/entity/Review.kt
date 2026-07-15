package com.aliexpressclone.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reviews",
    indices = [Index("productId"), Index("userId")]
)
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val userId: Long,
    val buyerName: String,
    val rating: Float,
    val comment: String,
    val createdAt: Long
)
