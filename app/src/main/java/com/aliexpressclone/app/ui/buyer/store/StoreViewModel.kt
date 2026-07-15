package com.aliexpressclone.app.ui.buyer.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.Product
import com.aliexpressclone.app.data.repository.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class StoreViewModel(
    productRepository: ProductRepository,
    val storeName: String
) : ViewModel() {

    val products: StateFlow<List<Product>> = productRepository.observeByStore(storeName)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
