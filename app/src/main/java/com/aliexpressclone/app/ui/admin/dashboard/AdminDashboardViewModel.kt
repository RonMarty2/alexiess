package com.aliexpressclone.app.ui.admin.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.OrderStatus
import com.aliexpressclone.app.data.repository.OrderRepository
import com.aliexpressclone.app.data.repository.ProductRepository
import com.aliexpressclone.app.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class AdminDashboardUiState(
    val productCount: Int = 0,
    val buyerCount: Int = 0,
    val pendingOrders: Int = 0,
    val totalOrders: Int = 0,
    val totalSales: Double = 0.0
)

class AdminDashboardViewModel(
    productRepository: ProductRepository,
    orderRepository: OrderRepository,
    userRepository: UserRepository
) : ViewModel() {

    val productCount: StateFlow<Int> = productRepository.observeAllProducts().map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val buyerCount: StateFlow<Int> = userRepository.observeBuyers().map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pendingOrders: StateFlow<Int> = orderRepository.observeAllOrders()
        .map { orders -> orders.count { it.status == OrderStatus.A_PAGAR } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalOrders: StateFlow<Int> = orderRepository.observeAllOrders().map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalSales: StateFlow<Double> = orderRepository.observeAllOrders()
        .map { orders -> orders.filter { it.status != OrderStatus.CANCELADO }.sumOf { it.total } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
}
