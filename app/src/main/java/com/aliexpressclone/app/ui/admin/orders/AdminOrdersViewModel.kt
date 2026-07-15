package com.aliexpressclone.app.ui.admin.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.Order
import com.aliexpressclone.app.data.repository.OrderRepository
import com.aliexpressclone.app.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class AdminOrderRow(
    val order: Order,
    val buyerName: String
)

class AdminOrdersViewModel(
    orderRepository: OrderRepository,
    userRepository: UserRepository
) : ViewModel() {

    val orders: StateFlow<List<AdminOrderRow>> = combine(
        orderRepository.observeAllOrders(),
        userRepository.observeBuyers()
    ) { orders, buyers ->
        val namesById = buyers.associate { it.id to it.name }
        orders.map { order -> AdminOrderRow(order, namesById[order.userId] ?: "Comprador #${order.userId}") }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
