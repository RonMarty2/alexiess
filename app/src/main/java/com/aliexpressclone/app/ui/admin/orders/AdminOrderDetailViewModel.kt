package com.aliexpressclone.app.ui.admin.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.Order
import com.aliexpressclone.app.data.local.entity.OrderItem
import com.aliexpressclone.app.data.local.entity.OrderStatus
import com.aliexpressclone.app.data.local.entity.OrderTrackingEvent
import com.aliexpressclone.app.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AdminOrderDetailViewModel(
    private val orderRepository: OrderRepository,
    orderId: Long
) : ViewModel() {

    val order: StateFlow<Order?> = orderRepository.observeOrder(orderId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val items: StateFlow<List<OrderItem>> = orderRepository.observeItems(orderId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tracking: StateFlow<List<OrderTrackingEvent>> = orderRepository.observeTracking(orderId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedStatus = MutableStateFlow<OrderStatus?>(null)
    val selectedStatus: StateFlow<OrderStatus?> = _selectedStatus

    private val _note = MutableStateFlow("")
    val note: StateFlow<String> = _note

    private val _estimatedDays = MutableStateFlow("7")
    val estimatedDays: StateFlow<String> = _estimatedDays

    private val _updateSuccessSignal = MutableStateFlow(0)
    val updateSuccessSignal: StateFlow<Int> = _updateSuccessSignal

    fun selectStatus(status: OrderStatus) {
        _selectedStatus.value = status
    }

    fun updateNote(value: String) {
        _note.value = value
    }

    fun updateEstimatedDays(value: String) {
        _estimatedDays.value = value
    }

    fun applyUpdate() {
        val currentOrder = order.value ?: return
        val newStatus = _selectedStatus.value ?: currentOrder.status
        val days = _estimatedDays.value.toLongOrNull() ?: 0
        val estimatedDeliveryDate = if (newStatus == OrderStatus.FINALIZADO || newStatus == OrderStatus.CANCELADO) {
            null
        } else {
            System.currentTimeMillis() + TimeUnit.DAYS.toMillis(days)
        }
        val noteText = _note.value.ifBlank { "Actualizado por el administrador: ${newStatus.label}." }

        viewModelScope.launch {
            orderRepository.updateStatus(currentOrder, newStatus, noteText, estimatedDeliveryDate)
            _note.value = ""
            _updateSuccessSignal.value += 1
        }
    }
}
