package com.aliexpressclone.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aliexpressclone.app.data.local.entity.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Insert
    suspend fun insert(review: Review): Long

    @Update
    suspend fun update(review: Review)

    @Query("SELECT * FROM reviews WHERE productId = :productId ORDER BY createdAt DESC")
    fun observeForProduct(productId: Long): Flow<List<Review>>

    @Query("SELECT * FROM reviews WHERE userId = :userId")
    fun observeByUser(userId: Long): Flow<List<Review>>

    @Query("SELECT * FROM reviews WHERE productId = :productId AND userId = :userId LIMIT 1")
    suspend fun getByProductAndUser(productId: Long, userId: Long): Review?
}
