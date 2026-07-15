package com.aliexpressclone.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aliexpressclone.app.data.local.entity.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)

    @Insert
    suspend fun insert(product: Product): Long

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("SELECT * FROM products ORDER BY id DESC")
    fun observeAll(): Flow<List<Product>>

    @Query("SELECT * FROM products ORDER BY id ASC")
    suspend fun getAllSnapshot(): List<Product>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<Product?>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Product?

    @Query("SELECT * FROM products WHERE storeName = :storeName ORDER BY id DESC")
    fun observeByStore(storeName: String): Flow<List<Product>>
}
