package com.aliexpressclone.app.ui.admin.sellers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.Seller
import com.aliexpressclone.app.data.repository.SellerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SellerFormState(
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val logoUri: String? = null,
    val isLoading: Boolean = true,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

class AdminSellerEditViewModel(
    private val sellerRepository: SellerRepository,
    private val sellerId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(SellerFormState())
    val uiState: StateFlow<SellerFormState> = _uiState

    init {
        viewModelScope.launch {
            if (sellerId != 0L) {
                val seller = sellerRepository.getById(sellerId)
                if (seller != null) {
                    _uiState.value = SellerFormState(
                        id = seller.id,
                        name = seller.name,
                        description = seller.description,
                        logoUri = seller.logoUri,
                        isLoading = false
                    )
                    return@launch
                }
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun update(transform: (SellerFormState) -> SellerFormState) {
        _uiState.value = transform(_uiState.value)
    }

    fun save() {
        val state = _uiState.value
        if (state.name.isBlank()) {
            _uiState.value = state.copy(errorMessage = "El nombre del vendedor es obligatorio.")
            return
        }
        viewModelScope.launch {
            val seller = Seller(
                id = state.id,
                name = state.name,
                description = state.description,
                logoUri = state.logoUri
            )
            if (state.id == 0L) {
                sellerRepository.create(seller)
            } else {
                sellerRepository.update(seller)
            }
            _uiState.value = state.copy(isSaved = true, errorMessage = null)
        }
    }
}
