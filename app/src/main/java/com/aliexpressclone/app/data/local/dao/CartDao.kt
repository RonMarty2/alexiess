package com.aliexpressclone.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aliexpressclone.app.data.local.entity.CartItem
import kotlinx.coroutines.flow.Flow

data class CartItemWithProduct(
    val cartItemId: Long,
    val productId: Long,
    val quantity: Int,
    val name: String,
    val price: Double,
    val originalPrice: Double,
    val storeName: String,
    val stock: Int,
    val placeholderEmoji: String,
    val placeholderColorHex: String,
    val imageUri: String?
)

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItem): Long

    @Update
    suspend fun update(item: CartItem)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearForUser(userId: Long)

    @Query("SELECT * FROM cart_items WHERE userId = :userId AND productId = :productId LIMIT 1")
    suspend fun findForProduct(userId: Long, productId: Long): CartItem?

    @Query(
        """
        SELECT c.id AS cartItemId, c.productId AS productId, c.quantity AS quantity,
               p.name AS name, p.price AS price, p.originalPrice AS originalPrice,
               p.storeName AS storeName, p.stock AS stock,
               p.placeholderEmoji AS placeholderEmoji, p.placeholderColorHex AS placeholderColorHex,
               p.imageUri AS imageUri
        FROM cart_items c
        INNER JOIN products p ON p.id = c.productId
        WHERE c.userId = :userId
        ORDER BY c.id DESC
        """
    )
    fun observeCartForUser(userId: Long): Flow<List<CartItemWithProduct>>
}
