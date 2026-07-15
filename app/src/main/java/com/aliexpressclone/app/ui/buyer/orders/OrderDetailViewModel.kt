package com.aliexpressclone.app.ui.buyer.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.Order
import com.aliexpressclone.app.data.local.entity.OrderItem
import com.aliexpressclone.app.data.local.entity.OrderTrackingEvent
import com.aliexpressclone.app.data.repository.OrderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class OrderDetailViewModel(
    orderRepository: OrderRepository,
    orderId: Long
) : ViewModel() {

    val order: StateFlow<Order?> = orderRepository.observeOrder(orderId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val items: StateFlow<List<OrderItem>> = orderRepository.observeItems(orderId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tracking: StateFlow<List<OrderTrackingEvent>> = orderRepository.observeTracking(orderId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
