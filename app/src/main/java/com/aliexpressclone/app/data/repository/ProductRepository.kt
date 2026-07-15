package com.aliexpressclone.app.data.repository

import com.aliexpressclone.app.data.local.dao.CategoryDao
import com.aliexpressclone.app.data.local.dao.ProductDao
import com.aliexpressclone.app.data.local.entity.Category
import com.aliexpressclone.app.data.local.entity.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao
) {
    fun observeAllProducts(): Flow<List<Product>> = productDao.observeAll()

    fun observeCategories(): Flow<List<Category>> = categoryDao.observeAll()

    fun observeProduct(id: Long): Flow<Product?> = productDao.observeById(id)

    fun observeByStore(storeName: String): Flow<List<Product>> = productDao.observeByStore(storeName)

    suspend fun getProduct(id: Long): Product? = productDao.getById(id)

    suspend fun createProduct(product: Product): Long = productDao.insert(product)

    suspend fun updateProduct(product: Product) = productDao.update(product)

    suspend fun deleteProduct(product: Product) = productDao.delete(product)

    suspend fun getCategories(): List<Category> = categoryDao.getAll()
}
