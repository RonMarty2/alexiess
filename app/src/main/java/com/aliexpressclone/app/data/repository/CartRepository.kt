package com.aliexpressclone.app.data.repository

import com.aliexpressclone.app.data.local.dao.CartDao
import com.aliexpressclone.app.data.local.dao.CartItemWithProduct
import com.aliexpressclone.app.data.local.entity.CartItem
import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartDao: CartDao) {

    fun observeCart(userId: Long): Flow<List<CartItemWithProduct>> = cartDao.observeCartForUser(userId)

    suspend fun addToCart(userId: Long, productId: Long, quantity: Int = 1) {
        val existing = cartDao.findForProduct(userId, productId)
        if (existing != null) {
            cartDao.update(existing.copy(quantity = existing.quantity + quantity))
        } else {
            cartDao.insert(CartItem(userId = userId, productId = productId, quantity = quantity))
        }
    }

    suspend fun setQuantity(cartItemId: Long, userId: Long, productId: Long, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteById(cartItemId)
        } else {
            cartDao.update(CartItem(id = cartItemId, userId = userId, productId = productId, quantity = quantity))
        }
    }

    suspend fun remove(cartItemId: Long) = cartDao.deleteById(cartItemId)

    suspend fun clear(userId: Long) = cartDao.clearForUser(userId)
}
