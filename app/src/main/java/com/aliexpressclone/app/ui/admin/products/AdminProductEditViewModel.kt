package com.aliexpressclone.app.ui.admin.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliexpressclone.app.data.local.entity.Category
import com.aliexpressclone.app.data.local.entity.Product
import com.aliexpressclone.app.data.local.entity.Seller
import com.aliexpressclone.app.data.repository.ProductRepository
import com.aliexpressclone.app.data.repository.SellerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProductFormState(
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val originalPrice: String = "",
    val stock: String = "",
    val storeName: String = "",
    val rating: String = "5.0",
    val soldCount: String = "0",
    val placeholderEmoji: String = "📦",
    val placeholderColorHex: String = "#E0E0E0",
    val imageUri: String? = null,
    val categoryId: Long? = null,
    val sellerId: Long? = null,
    val freeShipping: Boolean = true,
    val isLoading: Boolean = true,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

class AdminProductEditViewModel(
    private val productRepository: ProductRepository,
    private val sellerRepository: SellerRepository,
    private val productId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductFormState())
    val uiState: StateFlow<ProductFormState> = _uiState

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _sellers = MutableStateFlow<List<Seller>>(emptyList())
    val sellers: StateFlow<List<Seller>> = _sellers

    init {
        viewModelScope.launch {
            _categories.value = productRepository.getCategories()
            _sellers.value = sellerRepository.getAll()
            if (productId != 0L) {
                val product = productRepository.getProduct(productId)
                if (product != null) {
                    _uiState.value = ProductFormState(
                        id = product.id,
                        name = product.name,
                        description = product.description,
                        price = product.price.toString(),
                        originalPrice = product.originalPrice.toString(),
                        stock = product.stock.toString(),
                        storeName = product.storeName,
                        rating = product.rating.toString(),
                        soldCount = product.soldCount.toString(),
                        placeholderEmoji = product.placeholderEmoji,
                        placeholderColorHex = product.placeholderColorHex,
                        imageUri = product.imageUri,
                        categoryId = product.categoryId,
                        sellerId = product.sellerId,
                        freeShipping = product.freeShipping,
                        isLoading = false
                    )
                    return@launch
                }
            }
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                categoryId = _categories.value.firstOrNull()?.id
            )
        }
    }

    fun update(transform: (ProductFormState) -> ProductFormState) {
        _uiState.value = transform(_uiState.value)
    }

    fun selectSeller(seller: Seller) {
        _uiState.value = _uiState.value.copy(sellerId = seller.id, storeName = seller.name)
    }

    fun save() {
        val state = _uiState.value
        val price = state.price.toDoubleOrNull()
        val originalPrice = state.originalPrice.toDoubleOrNull()
        val stock = state.stock.toIntOrNull()
        val rating = state.rating.toFloatOrNull()
        val soldCount = state.soldCount.toIntOrNull()

        if (state.name.isBlank() || price == null || originalPrice == null || stock == null || rating == null || soldCount == null) {
            _uiState.value = state.copy(errorMessage = "Revisa que todos los campos numéricos sean válidos.")
            return
        }

        viewModelScope.launch {
            val product = Product(
                id = state.id,
                name = state.name,
                description = state.description,
                price = price,
                originalPrice = originalPrice,
                stock = stock,
                categoryId = state.categoryId,
                sellerId = state.sellerId,
                storeName = state.storeName.ifBlank { "Tienda del Administrador" },
                rating = rating,
                soldCount = soldCount,
                placeholderEmoji = state.placeholderEmoji.ifBlank { "📦" },
                placeholderColorHex = state.placeholderColorHex.ifBlank { "#E0E0E0" },
                imageUri = state.imageUri,
                freeShipping = state.freeShipping
            )
            if (state.id == 0L) {
                productRepository.createProduct(product)
            } else {
                productRepository.updateProduct(product)
            }
            _uiState.value = state.copy(isSaved = true, errorMessage = null)
        }
    }
}
