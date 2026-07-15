package com.aliexpressclone.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aliexpressclone.app.data.local.entity.Seller
import kotlinx.coroutines.flow.Flow

@Dao
interface SellerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sellers: List<Seller>)

    @Insert
    suspend fun insert(seller: Seller): Long

    @Update
    suspend fun update(seller: Seller)

    @Delete
    suspend fun delete(seller: Seller)

    @Query("SELECT * FROM sellers ORDER BY name")
    fun observeAll(): Flow<List<Seller>>

    @Query("SELECT * FROM sellers ORDER BY name")
    suspend fun getAll(): List<Seller>

    @Query("SELECT * FROM sellers WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Seller?
}
