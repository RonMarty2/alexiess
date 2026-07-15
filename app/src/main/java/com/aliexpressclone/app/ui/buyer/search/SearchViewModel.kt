package com.aliexpressclone.app.ui.buyer.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.Product
import com.aliexpressclone.app.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(
    productRepository: ProductRepository,
    initialQuery: String = "",
    val categoryId: Long? = null,
    val categoryName: String? = null
) : ViewModel() {

    val query = MutableStateFlow(initialQuery)

    val products: StateFlow<List<Product>> = combine(
        productRepository.observeAllProducts(),
        query
    ) { all, q ->
        all.filter { product ->
            (categoryId == null || product.categoryId == categoryId) &&
                (q.isBlank() || product.name.contains(q, ignoreCase = true) || product.storeName.contains(q, ignoreCase = true))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setQuery(value: String) {
        query.value = value
    }
}
