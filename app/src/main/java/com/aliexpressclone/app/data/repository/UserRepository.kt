package com.aliexpressclone.app.data.repository

import com.aliexpressclone.app.data.local.dao.UserDao
import com.aliexpressclone.app.data.local.entity.Role
import com.aliexpressclone.app.data.local.entity.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    suspend fun login(email: String, password: String): User? =
        userDao.login(email.trim(), password)

    suspend fun register(name: String, email: String, password: String, role: Role): Result<User> {
        val existing = userDao.findByEmail(email.trim())
        if (existing != null) {
            return Result.failure(IllegalArgumentException("Ya existe una cuenta con ese correo."))
        }
        val id = userDao.insert(
            User(name = name.trim(), email = email.trim(), password = password, role = role)
        )
        return Result.success(User(id = id, name = name.trim(), email = email.trim(), password = password, role = role))
    }

    suspend fun getById(id: Long): User? = userDao.findById(id)

    fun observeById(id: Long): Flow<User?> = userDao.observeById(id)

    fun observeBuyers(): Flow<List<User>> = userDao.observeByRole(Role.BUYER)
}
