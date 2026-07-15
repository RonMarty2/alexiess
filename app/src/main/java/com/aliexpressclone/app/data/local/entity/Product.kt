package com.aliexpressclone.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Seller::class,
            parentColumns = ["id"],
            childColumns = ["sellerId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("categoryId"), Index("sellerId")]
)
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val price: Double,
    val originalPrice: Double,
    val stock: Int,
    val categoryId: Long?,
    val sellerId: Long?,
    val storeName: String,
    val rating: Float,
    val soldCount: Int,
    val placeholderEmoji: String,
    val placeholderColorHex: String,
    val imageUri: String? = null,
    val freeShipping: Boolean = true
) {
    @get:Ignore
    val discountPercent: Int
        get() = if (originalPrice > price && originalPrice > 0) {
            (((originalPrice - price) / originalPrice) * 100).toInt()
        } else 0
}
