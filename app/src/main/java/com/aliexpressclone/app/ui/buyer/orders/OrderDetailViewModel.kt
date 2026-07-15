package com.aliexpressclone.app.ui.buyer.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.Order
import com.aliexpressclone.app.data.local.entity.OrderItem
import com.aliexpressclone.app.data.local.entity.OrderTrackingEvent
import com.aliexpressclone.app.data.local.entity.Review
import com.aliexpressclone.app.data.repository.OrderRepository
import com.aliexpressclone.app.data.repository.ReviewRepository
import com.aliexpressclone.app.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    orderRepository: OrderRepository,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
    private val userId: Long,
    orderId: Long
) : ViewModel() {

    val order: StateFlow<Order?> = orderRepository.observeOrder(orderId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val items: StateFlow<List<OrderItem>> = orderRepository.observeItems(orderId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tracking: StateFlow<List<OrderTrackingEvent>> = orderRepository.observeTracking(orderId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val myReviews: StateFlow<List<Review>> = reviewRepository.observeByUser(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun submitReview(productId: Long, rating: Float, comment: String) {
        viewModelScope.launch {
            val buyerName = userRepository.getById(userId)?.name ?: "Comprador"
            reviewRepository.submitReview(productId, userId, buyerName, rating, comment)
        }
    }
}
