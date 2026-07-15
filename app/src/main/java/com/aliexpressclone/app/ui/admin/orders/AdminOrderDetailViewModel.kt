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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

private val dateInputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)

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

    private val _purchaseDateText = MutableStateFlow("")
    val purchaseDateText: StateFlow<String> = _purchaseDateText

    private val _trackingNumberText = MutableStateFlow("")
    val trackingNumberText: StateFlow<String> = _trackingNumberText

    private val _updateSuccessSignal = MutableStateFlow(0)
    val updateSuccessSignal: StateFlow<Int> = _updateSuccessSignal

    init {
        viewModelScope.launch {
            val initialOrder = orderRepository.observeOrder(orderId).first()
            if (initialOrder != null) {
                _purchaseDateText.value = dateInputFormat.format(initialOrder.createdAt)
                _trackingNumberText.value = initialOrder.trackingNumber.orEmpty()
            }
        }
    }

    fun selectStatus(status: OrderStatus) {
        _selectedStatus.value = status
    }

    fun updateNote(value: String) {
        _note.value = value
    }

    fun updateEstimatedDays(value: String) {
        _estimatedDays.value = value
    }

    fun updatePurchaseDateText(value: String) {
        _purchaseDateText.value = value
    }

    fun updateTrackingNumberText(value: String) {
        _trackingNumberText.value = value
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

        viewModelScope.launch {
            orderRepository.updateStatus(currentOrder, newStatus, _note.value, estimatedDeliveryDate)
            _note.value = ""
            _selectedStatus.value = null
            _updateSuccessSignal.value += 1
        }
    }

    fun savePurchaseDate() {
        val currentOrder = order.value ?: return
        val parsedDate = runCatching { dateInputFormat.parse(_purchaseDateText.value) }.getOrNull() ?: return
        viewModelScope.launch {
            orderRepository.updateOrderDate(currentOrder, parsedDate.time)
        }
    }

    fun saveTrackingNumber() {
        val currentOrder = order.value ?: return
        viewModelScope.launch {
            orderRepository.updateTrackingNumber(currentOrder, _trackingNumberText.value.ifBlank { null })
        }
    }

    fun updateItemPhoto(item: OrderItem, imageUri: String?) {
        viewModelScope.launch {
            orderRepository.updateItemFulfillment(item, imageUri, item.realDescription)
        }
    }

    fun updateItemDescription(item: OrderItem, description: String) {
        viewModelScope.launch {
            orderRepository.updateItemFulfillment(item, item.imageUri, description)
        }
    }
}
