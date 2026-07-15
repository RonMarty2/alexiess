package com.aliexpressclone.app.ui.buyer.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.OrderItem
import com.aliexpressclone.app.data.local.entity.User
import com.aliexpressclone.app.data.repository.CartRepository
import com.aliexpressclone.app.data.repository.OrderRepository
import com.aliexpressclone.app.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CheckoutUiState(
    val shippingName: String = "",
    val shippingPhone: String = "",
    val shippingAddress: String = "",
    val paymentMethod: String = "Tarjeta de crédito (simulado)",
    val isPlacingOrder: Boolean = false,
    val placedOrderId: Long? = null
)

class CheckoutViewModel(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    userRepository: UserRepository,
    private val userId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState

    val total: StateFlow<Double> = cartRepository.observeCart(userId).map { items ->
        items.sumOf { it.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val itemCount: StateFlow<Int> = cartRepository.observeCart(userId).map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        viewModelScope.launch {
            val user: User? = userRepository.getById(userId)
            if (user != null) {
                _uiState.value = _uiState.value.copy(
                    shippingName = user.name,
                    shippingPhone = user.phone,
                    shippingAddress = user.address
                )
            }
        }
    }

    fun updateName(value: String) {
        _uiState.value = _uiState.value.copy(shippingName = value)
    }

    fun updatePhone(value: String) {
        _uiState.value = _uiState.value.copy(shippingPhone = value)
    }

    fun updateAddress(value: String) {
        _uiState.value = _uiState.value.copy(shippingAddress = value)
    }

    fun updatePaymentMethod(value: String) {
        _uiState.value = _uiState.value.copy(paymentMethod = value)
    }

    fun confirmPurchase() {
        val state = _uiState.value
        if (state.shippingName.isBlank() || state.shippingAddress.isBlank()) return

        viewModelScope.launch {
            _uiState.value = state.copy(isPlacingOrder = true)
            val cartSnapshot = cartRepository.observeCart(userId).first()
            if (cartSnapshot.isEmpty()) {
                _uiState.value = state.copy(isPlacingOrder = false)
                return@launch
            }
            val orderItems = cartSnapshot.map { item ->
                OrderItem(
                    orderId = 0,
                    productId = item.productId,
                    productName = item.name,
                    storeName = item.storeName,
                    unitPrice = item.price,
                    quantity = item.quantity,
                    placeholderEmoji = item.placeholderEmoji,
                    placeholderColorHex = item.placeholderColorHex,
                    imageUri = item.imageUri
                )
            }
            val total = cartSnapshot.sumOf { it.price * it.quantity }
            val orderId = orderRepository.placeOrderWithItems(
                userId = userId,
                shippingName = state.shippingName,
                shippingPhone = state.shippingPhone,
                shippingAddress = state.shippingAddress,
                items = orderItems,
                total = total
            )
            _uiState.value = state.copy(isPlacingOrder = false, placedOrderId = orderId)
        }
    }
}
