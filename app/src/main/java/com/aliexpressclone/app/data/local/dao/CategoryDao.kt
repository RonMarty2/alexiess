package com.aliexpressclone.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aliexpressclone.app.data.local.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<Category>)

    @Query("SELECT * FROM categories ORDER BY name")
    fun observeAll(): Flow<List<Category>>

    @Query("SELECT * FROM categories ORDER BY name")
    suspend fun getAll(): List<Category>
}
