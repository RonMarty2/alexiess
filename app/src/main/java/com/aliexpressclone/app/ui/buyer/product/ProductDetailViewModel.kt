package com.aliexpressclone.app.ui.buyer.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.Product
import com.aliexpressclone.app.data.local.entity.Review
import com.aliexpressclone.app.data.repository.CartRepository
import com.aliexpressclone.app.data.repository.ProductRepository
import com.aliexpressclone.app.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    reviewRepository: ReviewRepository,
    private val userId: Long,
    productId: Long
) : ViewModel() {

    val product: StateFlow<Product?> = productRepository.observeProduct(productId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val reviews: StateFlow<List<Review>> = reviewRepository.observeForProduct(productId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity

    private val _addedToCart = MutableStateFlow(false)
    val addedToCart: StateFlow<Boolean> = _addedToCart

    fun increaseQuantity() {
        val stock = product.value?.stock ?: Int.MAX_VALUE
        if (_quantity.value < stock) _quantity.value += 1
    }

    fun decreaseQuantity() {
        if (_quantity.value > 1) _quantity.value -= 1
    }

    fun addToCart() {
        val productId = product.value?.id ?: return
        viewModelScope.launch {
            cartRepository.addToCart(userId, productId, _quantity.value)
            _addedToCart.value = true
        }
    }

    fun consumeAddedToCartEvent() {
        _addedToCart.value = false
    }

    fun buyNow(onReady: () -> Unit) {
        val productId = product.value?.id ?: return
        viewModelScope.launch {
            cartRepository.addToCart(userId, productId, _quantity.value)
            onReady()
        }
    }
}
