package com.aliexpressclone.app.ui.buyer.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.dao.CartItemWithProduct
import com.aliexpressclone.app.data.repository.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository,
    private val userId: Long
) : ViewModel() {

    val cartItems: StateFlow<List<CartItemWithProduct>> = cartRepository.observeCart(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val total: StateFlow<Double> = cartItems.map { items ->
        items.sumOf { it.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun increase(item: CartItemWithProduct) {
        viewModelScope.launch {
            cartRepository.setQuantity(item.cartItemId, userId, item.productId, (item.quantity + 1).coerceAtMost(item.stock))
        }
    }

    fun decrease(item: CartItemWithProduct) {
        viewModelScope.launch {
            cartRepository.setQuantity(item.cartItemId, userId, item.productId, item.quantity - 1)
        }
    }

    fun remove(item: CartItemWithProduct) {
        viewModelScope.launch {
            cartRepository.remove(item.cartItemId)
        }
    }
}
