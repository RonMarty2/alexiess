package com.aliexpressclone.app.ui.buyer.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.Category
import com.aliexpressclone.app.data.local.entity.Product
import com.aliexpressclone.app.data.repository.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(productRepository: ProductRepository) : ViewModel() {

    val categories: StateFlow<List<Category>> = productRepository.observeCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val products: StateFlow<List<Product>> = productRepository.observeAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
