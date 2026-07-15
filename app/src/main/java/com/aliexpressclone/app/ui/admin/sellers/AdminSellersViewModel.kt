package com.aliexpressclone.app.ui.admin.sellers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.Seller
import com.aliexpressclone.app.data.repository.SellerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminSellersViewModel(private val sellerRepository: SellerRepository) : ViewModel() {

    val sellers: StateFlow<List<Seller>> = sellerRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteSeller(seller: Seller) {
        viewModelScope.launch { sellerRepository.delete(seller) }
    }
}
