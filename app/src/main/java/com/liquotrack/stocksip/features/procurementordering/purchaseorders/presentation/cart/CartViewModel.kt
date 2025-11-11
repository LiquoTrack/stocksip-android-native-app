package com.liquotrack.stocksip.features.procurementordering.purchaseorders.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.procurementordering.purchaseorders.data.local.CartItemEntity
import com.liquotrack.stocksip.features.procurementordering.purchaseorders.domain.repositories.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartItem(
    val id: Int,
    val productId: String,
    val productName: String,
    val unitPrice: Double,
    val imageUrl: String?,
    val quantity: Int
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _subTotal = MutableStateFlow(0.0)
    val subTotal: StateFlow<String> = _subTotal.map { "$${"%.2f".format(it)}" }
        .stateIn(viewModelScope, SharingStarted.Lazily, "$0.00")

    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<String> = _total.map { "$${"%.2f".format(it)}" }
        .stateIn(viewModelScope, SharingStarted.Lazily, "$0.00")

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadCart()
    }

    private fun loadCart() {
        viewModelScope.launch {
            cartRepository.getAllCartItems()
                .collect { entities ->
                    val items = entities.map { it.toCartItem() }
                    _cartItems.value = items
                    recalcTotals(items)
                }
        }
    }

    private fun recalcTotals(items: List<CartItem>) {
        val subtotal = items.sumOf { it.unitPrice * it.quantity }
        _subTotal.value = subtotal
        _total.value = subtotal
    }

    fun increaseQuantity(item: CartItem) {
        viewModelScope.launch {
            val entity = _cartItems.value.find { it.id == item.id }?.toEntity() ?: return@launch
            cartRepository.updateQuantity(entity, entity.quantity + 1)
            loadCart()
        }
    }

    fun decreaseQuantity(item: CartItem) {
        if (item.quantity <= 1) return
        viewModelScope.launch {
            val entity = _cartItems.value.find { it.id == item.id }?.toEntity() ?: return@launch
            cartRepository.updateQuantity(entity, entity.quantity - 1)
            loadCart()
        }
    }

    fun removeItem(item: CartItem) {
        viewModelScope.launch {
            val entity = _cartItems.value.find { it.id == item.id }?.toEntity() ?: return@launch
            cartRepository.removeFromCart(entity)
            loadCart()
        }
    }
}

private fun CartItemEntity.toCartItem(): CartItem =
    CartItem(
        id = this.id,
        productId = this.productId,
        productName = this.productName,
        unitPrice = this.unitPrice ?: 0.0,
        imageUrl = this.imageUrl,
        quantity = this.quantity
    )

private fun CartItem.toEntity(): CartItemEntity =
    CartItemEntity(
        id = this.id,
        productId = this.productId,
        productName = this.productName,
        unitPrice = this.unitPrice,
        imageUrl = this.imageUrl,
        quantity = this.quantity,
        subTotal = this.unitPrice * this.quantity
    )
