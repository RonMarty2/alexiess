package com.aliexpressclone.app.data.repository

import com.aliexpressclone.app.data.local.dao.SellerDao
import com.aliexpressclone.app.data.local.entity.Seller
import kotlinx.coroutines.flow.Flow

class SellerRepository(private val sellerDao: SellerDao) {

    fun observeAll(): Flow<List<Seller>> = sellerDao.observeAll()

    suspend fun getAll(): List<Seller> = sellerDao.getAll()

    suspend fun getById(id: Long): Seller? = sellerDao.getById(id)

    suspend fun create(seller: Seller): Long = sellerDao.insert(seller)

    suspend fun update(seller: Seller) = sellerDao.update(seller)

    suspend fun delete(seller: Seller) = sellerDao.delete(seller)
}
