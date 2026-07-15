package com.aliexpressclone.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aliexpressclone.app.data.local.entity.Role
import com.aliexpressclone.app.data.local.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): User?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun observeById(id: Long): Flow<User?>

    @Query("SELECT * FROM users WHERE role = :role ORDER BY name")
    fun observeByRole(role: Role): Flow<List<User>>

    @Query("SELECT COUNT(*) FROM users")
    suspend fun count(): Int
}
