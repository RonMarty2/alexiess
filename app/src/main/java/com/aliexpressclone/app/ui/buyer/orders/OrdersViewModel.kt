package com.aliexpressclone.app.ui.buyer.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.Order
import com.aliexpressclone.app.data.local.entity.OrderStatus
import com.aliexpressclone.app.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class OrdersViewModel(
    orderRepository: OrderRepository,
    userId: Long,
    initialStatus: OrderStatus? = null
) : ViewModel() {

    private val allOrders: StateFlow<List<Order>> = orderRepository.observeOrdersForUser(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedStatus = MutableStateFlow(initialStatus)
    val selectedStatus: StateFlow<OrderStatus?> = _selectedStatus

    val filteredOrders: StateFlow<List<Order>> = combine(allOrders, _selectedStatus) { orders, status ->
        if (status == null) orders else orders.filter { it.status == status }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectStatus(status: OrderStatus?) {
        _selectedStatus.value = status
    }
}
