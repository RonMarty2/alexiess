package com.aliexpressclone.app.ui.admin.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.OrderStatus
import com.aliexpressclone.app.data.repository.OrderRepository
import com.aliexpressclone.app.data.repository.ProductRepository
import com.aliexpressclone.app.data.repository.SellerRepository
import com.aliexpressclone.app.data.repository.UserRepository
import com.aliexpressclone.app.data.seed.CatalogExporter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminDashboardViewModel(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val sellerRepository: SellerRepository,
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

    private val _resetInProgress = MutableStateFlow(false)
    val resetInProgress: StateFlow<Boolean> = _resetInProgress

    private val _exportInProgress = MutableStateFlow(false)
    val exportInProgress: StateFlow<Boolean> = _exportInProgress

    fun resetTransactionalData() {
        viewModelScope.launch {
            _resetInProgress.value = true
            orderRepository.resetTransactionalData()
            _resetInProgress.value = false
        }
    }

    fun exportCatalog(onReady: (String) -> Unit) {
        viewModelScope.launch {
            _exportInProgress.value = true
            val json = CatalogExporter.buildJson(productRepository, sellerRepository)
            _exportInProgress.value = false
            onReady(json)
        }
    }
}
