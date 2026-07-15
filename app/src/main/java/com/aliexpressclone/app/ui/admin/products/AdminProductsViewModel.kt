package com.aliexpressclone.app.ui.admin.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.Product
import com.aliexpressclone.app.data.repository.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminProductsViewModel(private val productRepository: ProductRepository) : ViewModel() {

    val products: StateFlow<List<Product>> = productRepository.observeAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteProduct(product: Product) {
        viewModelScope.launch { productRepository.deleteProduct(product) }
    }
}
